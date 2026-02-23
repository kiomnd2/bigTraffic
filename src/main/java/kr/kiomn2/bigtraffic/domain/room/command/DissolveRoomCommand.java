package kr.kiomn2.bigtraffic.domain.room.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DissolveRoomCommand {
    private final Long userId;
    private final Long roomId;
}
