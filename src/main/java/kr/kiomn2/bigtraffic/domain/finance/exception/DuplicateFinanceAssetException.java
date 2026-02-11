package kr.kiomn2.bigtraffic.domain.finance.exception;

import kr.kiomn2.bigtraffic.common.exception.BusinessException;
import kr.kiomn2.bigtraffic.common.exception.ErrorCode;

public class DuplicateFinanceAssetException extends BusinessException {

    public DuplicateFinanceAssetException() {
        super(ErrorCode.DUPLICATE_FINANCE_ASSET);
    }

    public DuplicateFinanceAssetException(String message) {
        super(ErrorCode.DUPLICATE_FINANCE_ASSET, message);
    }
}
