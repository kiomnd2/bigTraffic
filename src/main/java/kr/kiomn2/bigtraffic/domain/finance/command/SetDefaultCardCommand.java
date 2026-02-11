package kr.kiomn2.bigtraffic.domain.finance.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SetDefaultCardCommand {
    private final Long userId;
    private final Long cardId;
}
