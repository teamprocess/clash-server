package com.process.clash.application.user.userstudytime.port.out;

import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;

public interface UserStudyTimeRepositoryPort {

    UserStudyTime save(UserStudyTime userStudyTime);
}
