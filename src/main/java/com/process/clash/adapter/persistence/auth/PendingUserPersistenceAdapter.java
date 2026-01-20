package com.process.clash.adapter.persistence.auth;

import com.process.clash.application.user.user.port.out.PendingUserCachePort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class PendingUserPersistenceAdapter implements PendingUserCachePort {

    private final RedisTemplate<String, PendingUserDto> redisTemplate;
    private static final String PENDING_USER_PREFIX = "pending_user:";


    @Override
    public void save(String token, User user, long ttlMs) {
        PendingUserDto pendingUserDto = PendingUserDto.from(user);
        redisTemplate.opsForValue().set(PENDING_USER_PREFIX + token, pendingUserDto, ttlMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<User> findByToken(String token) {
        PendingUserDto dto = redisTemplate.opsForValue().get(PENDING_USER_PREFIX + token);
        return Optional.ofNullable(dto).map(PendingUserDto::toUser);
    }

    @Override
    public void delete(String token) {
        redisTemplate.delete(PENDING_USER_PREFIX + token);
    }
}
