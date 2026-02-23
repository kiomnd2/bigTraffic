package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class RoomAccountBookAlreadyLinkedException extends BusinessException {

    public RoomAccountBookAlreadyLinkedException() {
        super(ErrorCode.ROOM_ACCOUNT_BOOK_ALREADY_LINKED);
    }

    public RoomAccountBookAlreadyLinkedException(String message) {
        super(ErrorCode.ROOM_ACCOUNT_BOOK_ALREADY_LINKED, message);
    }
}
