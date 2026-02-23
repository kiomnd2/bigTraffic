package kr.kiomn2.bigtraffic.domain.room.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRoomCommand {
    private final Long userId;
    private final Long roomId;
    private final String name;
    private final String description;
    private final Integer maxMembers;
}
