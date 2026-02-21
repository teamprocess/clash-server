package com.process.clash.application.user.usernotice.port.in;

import com.process.clash.application.user.usernotice.data.ReadUserNoticeData;

public interface ReadUserNoticeUseCase {

    void execute(ReadUserNoticeData.Command command);
}
