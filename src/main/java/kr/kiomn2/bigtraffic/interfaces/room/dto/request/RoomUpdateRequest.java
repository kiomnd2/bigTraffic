package kr.kiomn2.bigtraffic.interfaces.room.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateRequest {

    private String name;

    private String description;

    private Integer maxMembers;
}
