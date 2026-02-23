package kr.kiomn2.bigtraffic.interfaces.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateRequest {

    @NotBlank(message = "방 이름은 필수입니다.")
    private String name;

    private String description;

    private Integer maxMembers;
}
