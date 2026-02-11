package kr.kiomn2.bigtraffic.domain.finance.service;

public interface FinanceDataEncryptor {

    String encrypt(String plainText);

    String decrypt(String encryptedText);

    String maskAccountNumber(String accountNumber);

    String maskCardNumber(String cardNumber);

    String extractLastFourDigits(String number);
}
