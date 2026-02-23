package kr.kiomn2.bigtraffic.domain.room.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class RoomMemberLimitExceededException extends BusinessException {

    public RoomMemberLimitExceededException() {
        super(ErrorCode.ROOM_MEMBER_LIMIT_EXCEEDED);
    }

    public RoomMemberLimitExceededException(String message) {
        super(ErrorCode.ROOM_MEMBER_LIMIT_EXCEEDED, message);
    }
}
