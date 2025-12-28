package kr.kiomn2.bigtraffic.interfaces.auth.dto;

import lombok.Data;

@Data
public class KakaoCallbackRequest {
    private String code;
}
