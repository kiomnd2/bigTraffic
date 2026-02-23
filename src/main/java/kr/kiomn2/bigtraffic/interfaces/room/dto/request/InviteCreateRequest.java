package kr.kiomn2.bigtraffic.interfaces.room.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteCreateRequest {

    private Integer expirationHours;
}
