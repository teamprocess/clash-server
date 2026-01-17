package com.process.clash.application.roadmap.chapterprogress.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.chapterprogress.exception.status.UserChapterProgressStatusCode;

public class UserChapterProgressNotFoundException extends NotFoundException {
    public UserChapterProgressNotFoundException() {
        super(UserChapterProgressStatusCode.USER_CHAPTER_PROGRESS_NOT_FOUND);
    }

    public UserChapterProgressNotFoundException(Throwable cause) {
        super(UserChapterProgressStatusCode.USER_CHAPTER_PROGRESS_NOT_FOUND, cause);
    }
}
