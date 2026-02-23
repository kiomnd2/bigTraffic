package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class RoomMemberNotFoundException extends BusinessException {

    public RoomMemberNotFoundException() {
        super(ErrorCode.ROOM_MEMBER_NOT_FOUND);
    }

    public RoomMemberNotFoundException(String message) {
        super(ErrorCode.ROOM_MEMBER_NOT_FOUND, message);
    }
}
