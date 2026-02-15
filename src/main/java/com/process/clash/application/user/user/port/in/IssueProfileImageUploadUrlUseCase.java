package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.IssueProfileImageUploadUrlData;

public interface IssueProfileImageUploadUrlUseCase {
    IssueProfileImageUploadUrlData.Result execute(IssueProfileImageUploadUrlData.Command command);
}
