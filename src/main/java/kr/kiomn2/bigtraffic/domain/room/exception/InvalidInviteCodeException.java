package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class InvalidInviteCodeException extends BusinessException {

    public InvalidInviteCodeException() {
        super(ErrorCode.INVALID_INVITE_CODE);
    }

    public InvalidInviteCodeException(String message) {
        super(ErrorCode.INVALID_INVITE_CODE, message);
    }
}
