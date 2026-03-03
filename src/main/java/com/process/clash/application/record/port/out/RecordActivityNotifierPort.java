package com.process.clash.application.record.port.out;

import com.process.clash.application.common.actor.Actor;

public interface RecordActivityNotifierPort {

    void notifyActivityStarted(Actor actor);

    void notifyActivityStopped(Actor actor);

    void notifySessionChanged(Actor actor);
}
