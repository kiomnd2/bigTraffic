package kr.kiomn2.bigtraffic.domain.auth.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }

    public DuplicateEmailException(String message) {
        super(ErrorCode.DUPLICATE_EMAIL, message);
    }
}