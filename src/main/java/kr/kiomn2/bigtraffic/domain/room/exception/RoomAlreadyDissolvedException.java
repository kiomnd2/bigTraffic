package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class RoomAlreadyDissolvedException extends BusinessException {

    public RoomAlreadyDissolvedException() {
        super(ErrorCode.ROOM_ALREADY_DISSOLVED);
    }

    public RoomAlreadyDissolvedException(String message) {
        super(ErrorCode.ROOM_ALREADY_DISSOLVED, message);
    }
}
