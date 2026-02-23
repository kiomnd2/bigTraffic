package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class RoomNotFoundException extends BusinessException {

    public RoomNotFoundException() {
        super(ErrorCode.ROOM_NOT_FOUND);
    }

    public RoomNotFoundException(String message) {
        super(ErrorCode.ROOM_NOT_FOUND, message);
    }
}
