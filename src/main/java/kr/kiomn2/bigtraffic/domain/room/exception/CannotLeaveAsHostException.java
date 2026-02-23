package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class CannotLeaveAsHostException extends BusinessException {

    public CannotLeaveAsHostException() {
        super(ErrorCode.CANNOT_LEAVE_AS_HOST);
    }

    public CannotLeaveAsHostException(String message) {
        super(ErrorCode.CANNOT_LEAVE_AS_HOST, message);
    }
}
