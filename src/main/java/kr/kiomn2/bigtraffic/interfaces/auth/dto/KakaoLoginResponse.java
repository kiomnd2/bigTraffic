package kr.kiomn2.bigtraffic.interfaces.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoLoginResponse {
    private String token;
    private String message;
}
