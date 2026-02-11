package kr.kiomn2.bigtraffic.domain.auth.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogoutCommand {
    private final String token;
}
