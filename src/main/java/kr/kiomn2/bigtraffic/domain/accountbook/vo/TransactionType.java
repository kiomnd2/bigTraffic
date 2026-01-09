package kr.kiomn2.bigtraffic.domain.accountbook.vo;

/**
 * 거래 유형
 * INCOME: 수입
 * EXPENSE: 지출
 */
public enum TransactionType {
    INCOME("수입"),
    EXPENSE("지출");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
