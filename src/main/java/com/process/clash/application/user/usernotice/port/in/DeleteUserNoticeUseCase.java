package com.process.clash.application.user.usernotice.port.in;

import com.process.clash.application.user.usernotice.data.DeleteUserNoticeData;

public interface DeleteUserNoticeUseCase {

    void execute(DeleteUserNoticeData.Command command);
}