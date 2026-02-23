package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class RoomHostPermissionException extends BusinessException {

    public RoomHostPermissionException() {
        super(ErrorCode.ROOM_HOST_PERMISSION_REQUIRED);
    }

    public RoomHostPermissionException(String message) {
        super(ErrorCode.ROOM_HOST_PERMISSION_REQUIRED, message);
    }
}
