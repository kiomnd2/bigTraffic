package kr.kiomn2.bigtraffic.infrastructure.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("=== BusinessException 발생 ===");
        log.error("ErrorCode: {}", e.getErrorCode());
        log.error("Message: {}", e.getMessage());
        log.error("StackTrace: ", e);
        log.error("================================");

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                errorCode.getStatus().value()
        );
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    /**
     * IllegalArgumentException 처리 (기존 코드 호환성)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("=== IllegalArgumentException 발생 ===");
        log.error("Message: {}", e.getMessage());
        log.error("StackTrace: ", e);
        log.error("===========================================");

        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * OAuth2 인증 예외 처리
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        log.error("=== OAuth2AuthenticationException 발생 ===");
        log.error("Error Code: {}", e.getError().getErrorCode());
        log.error("Error Description: {}", e.getError().getDescription());
        log.error("Error URI: {}", e.getError().getUri());
        log.error("Message: {}", e.getMessage());
        log.error("StackTrace: ", e);
        log.error("================================================");

        ErrorResponse response = new ErrorResponse(
                "OAuth2 로그인에 실패했습니다: " + e.getError().getDescription(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    /**
     * Spring Security 인증 예외 처리
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("=== AuthenticationException 발생 ===");
        log.error("Exception Type: {}", e.getClass().getSimpleName());
        log.error("Message: {}", e.getMessage());
        log.error("StackTrace: ", e);
        log.error("===========================================");

        ErrorResponse response = new ErrorResponse(
                "인증에 실패했습니다: " + e.getMessage(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    /**
     * 유효성 검증 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("=== MethodArgumentNotValidException 발생 ===");
        log.error("Validation Errors: {}", errorMessage);
        log.error("Object Name: {}", e.getBindingResult().getObjectName());
        log.error("Error Count: {}", e.getBindingResult().getErrorCount());
        log.error("StackTrace: ", e);
        log.error("===================================================");

        ErrorResponse response = new ErrorResponse(
                "입력값 검증에 실패했습니다: " + errorMessage,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 데이터베이스 접근 예외 처리
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        log.error("=== DataAccessException 발생 ===");
        log.error("Exception Type: {}", e.getClass().getSimpleName());
        log.error("Message: {}", e.getMessage());
        log.error("Root Cause: {}", e.getRootCause() != null ? e.getRootCause().getMessage() : "N/A");
        if (e.getRootCause() != null) {
            log.error("Root Cause StackTrace: ", e.getRootCause());
        }
        log.error("StackTrace: ", e);
        log.error("======================================");

        ErrorResponse response = new ErrorResponse(
                "데이터베이스 처리 중 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    /**
     * NullPointerException 처리
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("=== NullPointerException 발생 ===");
        log.error("Message: {}", e.getMessage());

        // NPE는 특히 어디서 발생했는지가 중요하므로 스택 트레이스 전체 출력
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            log.error("발생 위치: {}:{}", stackTrace[0].getClassName(), stackTrace[0].getLineNumber());
            log.error("메서드: {}", stackTrace[0].getMethodName());
        }

        log.error("전체 StackTrace: ", e);
        log.error("===================================");

        ErrorResponse response = new ErrorResponse(
                "서버 내부 오류가 발생했습니다. (NPE)",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("=== 예상치 못한 Exception 발생 ===");
        log.error("Exception Type: {}", e.getClass().getName());
        log.error("Message: {}", e.getMessage());

        // 스택 트레이스에서 중요 정보 추출
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            log.error("발생 위치: {}:{}", stackTrace[0].getClassName(), stackTrace[0].getLineNumber());
            log.error("메서드: {}", stackTrace[0].getMethodName());
            log.error("파일: {}", stackTrace[0].getFileName());
        }

        // Cause 체인 로깅
        Throwable cause = e.getCause();
        int depth = 0;
        while (cause != null && depth < 5) {
            log.error("Caused by [{}]: {} - {}", depth, cause.getClass().getName(), cause.getMessage());
            cause = cause.getCause();
            depth++;
        }

        log.error("전체 StackTrace: ", e);
        log.error("==========================================");

        ErrorResponse response = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
