package kr.kiomn2.bigtraffic.domain.accountbook.vo;

/**
 * 결제 수단
 */
public enum PaymentMethod {
    CASH("현금"),
    ACCOUNT("계좌"),
    CARD("카드");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
