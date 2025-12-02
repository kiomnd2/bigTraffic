package kr.kiomn2.bigtraffic.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String message;

    public AuthResponse(String token) {
        this.token = token;
        this.message = "success";
    }
}