package kr.kiomn2.bigtraffic.domain.finance.exception;

import kr.kiomn2.bigtraffic.domain.auth.exception.BusinessException;
import kr.kiomn2.bigtraffic.domain.auth.exception.ErrorCode;

public class CardNotFoundException extends BusinessException {

    public CardNotFoundException() {
        super(ErrorCode.CARD_NOT_FOUND);
    }

    public CardNotFoundException(String message) {
        super(ErrorCode.CARD_NOT_FOUND, message);
    }
}
