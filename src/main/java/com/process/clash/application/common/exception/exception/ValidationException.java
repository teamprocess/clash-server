package com.process.clash.application.common.exception.exception;

// 400이지만 의미가 달라서 따로 구현 @Valid에서 걸렸을때 검증함
public abstract class ValidationException extends BadRequestException {
        // 내용이 없는 추상 클래스이며 의도는 디버깅시 로그 탐색을 편하게 하기 위한 분기점 추가입니다.
}