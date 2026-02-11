package kr.kiomn2.bigtraffic.domain.auth.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}