package com.process.clash.application.realtime.service;

import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.in.ReportUserPresenceUseCase;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserPresenceService implements ReportUserPresenceUseCase, UserPresencePort {

    private final Object monitor = new Object();
    private final Map<String, SessionPresence> sessionByConnectionId = new HashMap<>();
    private final Map<Long, PresenceCounter> counterByUserId = new HashMap<>();

    @Override
    public void connected(String connectionId, Long userId) {
        if (isInvalid(connectionId, userId)) {
            return;
        }

        synchronized (monitor) {
            SessionPresence previous = sessionByConnectionId.put(
                connectionId,
                SessionPresence.online(userId)
            );

            if (previous != null) {
                decrease(previous.userId(), previous.away());
            }
            increase(userId, false);
        }
    }

    @Override
    public void disconnected(String connectionId) {
        if (isBlank(connectionId)) {
            return;
        }

        synchronized (monitor) {
            SessionPresence removed = sessionByConnectionId.remove(connectionId);
            if (removed == null) {
                return;
            }
            decrease(removed.userId(), removed.away());
        }
    }

    @Override
    public void markedAway(String connectionId) {
        markAwayState(connectionId, true);
    }

    @Override
    public void markedOnline(String connectionId) {
        markAwayState(connectionId, false);
    }

    @Override
    public UserActivityStatus getStatus(Long userId) {
        if (userId == null) {
            return UserActivityStatus.OFFLINE;
        }

        synchronized (monitor) {
            return resolveStatus(counterByUserId.get(userId));
        }
    }

    @Override
    public Map<Long, UserActivityStatus> getStatuses(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        synchronized (monitor) {
            Map<Long, UserActivityStatus> result = new HashMap<>();
            for (Long userId : userIds) {
                if (userId == null) {
                    continue;
                }
                result.put(userId, resolveStatus(counterByUserId.get(userId)));
            }
            return result;
        }
    }

    private void markAwayState(String connectionId, boolean away) {
        if (isBlank(connectionId)) {
            return;
        }

        synchronized (monitor) {
            SessionPresence current = sessionByConnectionId.get(connectionId);
            if (current == null || current.away() == away) {
                return;
            }

            sessionByConnectionId.put(connectionId, current.changeAway(away));
            if (away) {
                increaseAway(current.userId());
            } else {
                decreaseAway(current.userId());
            }
        }
    }

    private void increase(Long userId, boolean away) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null) {
            counter = new PresenceCounter();
            counterByUserId.put(userId, counter);
        }
        counter.connectedSessions++;
        if (away) {
            counter.awaySessions++;
        }
    }

    private void decrease(Long userId, boolean away) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null) {
            return;
        }

        counter.connectedSessions = Math.max(0, counter.connectedSessions - 1);
        if (away) {
            counter.awaySessions = Math.max(0, counter.awaySessions - 1);
        }

        if (counter.connectedSessions == 0) {
            counterByUserId.remove(userId);
            return;
        }

        if (counter.awaySessions > counter.connectedSessions) {
            counter.awaySessions = counter.connectedSessions;
        }
    }

    private void increaseAway(Long userId) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null || counter.connectedSessions == 0) {
            return;
        }
        counter.awaySessions = Math.min(counter.connectedSessions, counter.awaySessions + 1);
    }

    private void decreaseAway(Long userId) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null) {
            return;
        }
        counter.awaySessions = Math.max(0, counter.awaySessions - 1);
    }

    private UserActivityStatus resolveStatus(PresenceCounter counter) {
        if (counter == null || counter.connectedSessions == 0) {
            return UserActivityStatus.OFFLINE;
        }
        if (counter.awaySessions >= counter.connectedSessions) {
            return UserActivityStatus.AWAY;
        }
        return UserActivityStatus.ONLINE;
    }

    private boolean isInvalid(String connectionId, Long userId) {
        return isBlank(connectionId) || userId == null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record SessionPresence(Long userId, boolean away) {
        private static SessionPresence online(Long userId) {
            return new SessionPresence(userId, false);
        }

        private SessionPresence changeAway(boolean away) {
            return new SessionPresence(this.userId, away);
        }
    }

    private static final class PresenceCounter {
        private int connectedSessions;
        private int awaySessions;
    }
}
