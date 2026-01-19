package com.process.clash.application.user.userstudytime.port.out;

import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;

import java.time.LocalDate;
import java.util.Optional;

public interface UserStudyTimeRepositoryPort {

    UserStudyTime save(UserStudyTime userStudyTime);
    Optional<UserStudyTime> findByUserIdAndDate(Long userId, LocalDate date);
}
