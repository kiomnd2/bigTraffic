package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class AlreadyRoomMemberException extends BusinessException {

    public AlreadyRoomMemberException() {
        super(ErrorCode.ALREADY_ROOM_MEMBER);
    }

    public AlreadyRoomMemberException(String message) {
        super(ErrorCode.ALREADY_ROOM_MEMBER, message);
    }
}
