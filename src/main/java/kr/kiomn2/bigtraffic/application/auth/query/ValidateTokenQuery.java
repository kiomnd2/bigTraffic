package kr.kiomn2.bigtraffic.application.auth.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidateTokenQuery {
    private final String token;
}
