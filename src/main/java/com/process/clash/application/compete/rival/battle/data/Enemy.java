package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.domain.user.user.entity.User;

public record Enemy(
            Long id,
            String name,
            String profileImage
    ) {

        public static Enemy from(User user) {

            return new Enemy(
                    user.id(),
                    user.name(),
                    user.profileImage()
            );
        }
    }