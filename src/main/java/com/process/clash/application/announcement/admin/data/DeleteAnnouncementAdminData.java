package com.process.clash.application.announcement.admin.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteAnnouncementAdminData {

    public record Command(Actor actor, Long id) {}
}
