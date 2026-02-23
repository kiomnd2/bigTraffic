package kr.kiomn2.bigtraffic.domain.room.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KickMemberCommand {
    private final Long userId;
    private final Long roomId;
    private final Long targetUserId;
}
