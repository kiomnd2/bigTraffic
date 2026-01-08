package kr.kiomn2.bigtraffic.domain.finance.exception;

import kr.kiomn2.bigtraffic.domain.auth.exception.BusinessException;
import kr.kiomn2.bigtraffic.domain.auth.exception.ErrorCode;

public class BankAccountNotFoundException extends BusinessException {

    public BankAccountNotFoundException() {
        super(ErrorCode.BANK_ACCOUNT_NOT_FOUND);
    }

    public BankAccountNotFoundException(String message) {
        super(ErrorCode.BANK_ACCOUNT_NOT_FOUND, message);
    }
}
