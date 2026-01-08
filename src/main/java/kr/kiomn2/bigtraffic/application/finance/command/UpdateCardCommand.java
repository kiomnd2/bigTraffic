package kr.kiomn2.bigtraffic.application.finance.command;

import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.CardUpdateRequest;
import lombok.Getter;

@Getter
public class UpdateCardCommand {
    private final Long userId;
    private final Long cardId;
    private final String cardName;
    private final String color;
    private final String memo;

    private UpdateCardCommand(Long userId, Long cardId, String cardName,
                              String color, String memo) {
        this.userId = userId;
        this.cardId = cardId;
        this.cardName = cardName;
        this.color = color;
        this.memo = memo;
    }

    public static UpdateCardCommand from(Long userId, Long cardId, CardUpdateRequest request) {
        return new UpdateCardCommand(
                userId,
                cardId,
                request.getCardName(),
                request.getColor(),
                request.getMemo()
        );
    }
}
