package kr.kiomn2.bigtraffic.domain.room.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinRoomCommand {
    private final Long userId;
    private final String inviteCode;
}
