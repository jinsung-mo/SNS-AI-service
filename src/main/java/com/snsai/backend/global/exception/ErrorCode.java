package com.snsai.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    // 글로벌 에러 (기본적으로 발생할 수 있는 에러)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_500", "서버 내부에서 오류가 발생했습니다."),
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "GLOBAL_400", "잘못된 요청입니다."),
    
    // 회원 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER_409", "이미 사용 중인 이메일입니다."),
    
    // 구독 및 사용량 관련 에러
    USAGE_LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, "SUBSCRIPTION_403", "무료 플랜의 이번 달 사용량을 모두 소진했습니다. 프로 플랜으로 업그레이드 해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
