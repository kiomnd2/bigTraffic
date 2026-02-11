package kr.kiomn2.bigtraffic.domain.auth.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetCurrentUserQuery {
    private final String email;
}
