package com.snsai.backend;

import com.snsai.backend.global.common.ApiResponse;
import com.snsai.backend.global.exception.CustomException;
import com.snsai.backend.global.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.success("Hello, World! 백엔드 서버가 정상적으로 실행 중입니다.");
    }

    // 예외 처리 테스트용 API
    @GetMapping("/error-test")
    public ApiResponse<String> errorTest(@RequestParam(required = false) String type) {
        if ("custom".equals(type)) {
            // 우리가 만든 CustomException을 일부러 발생시킵니다.
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } else {
            // 예상치 못한 에러(NullPointerException)를 일부러 발생시킵니다.
            String nullString = null;
            nullString.length(); 
            return ApiResponse.success("이 메시지는 출력되지 않습니다.");
        }
    }
}
