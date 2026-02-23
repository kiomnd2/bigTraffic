package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class RoomAccountBookNotFoundException extends BusinessException {

    public RoomAccountBookNotFoundException() {
        super(ErrorCode.ROOM_ACCOUNT_BOOK_NOT_FOUND);
    }

    public RoomAccountBookNotFoundException(String message) {
        super(ErrorCode.ROOM_ACCOUNT_BOOK_NOT_FOUND, message);
    }
}
