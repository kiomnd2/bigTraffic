package kr.kiomn2.bigtraffic.interfaces.finance.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateRequest {

    private String cardName;

    private String color;

    private String memo;
}
