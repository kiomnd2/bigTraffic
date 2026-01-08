package kr.kiomn2.bigtraffic.interfaces.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CardListResponse {

    private List<CardResponse> cards;
    private Integer totalCount;
}
