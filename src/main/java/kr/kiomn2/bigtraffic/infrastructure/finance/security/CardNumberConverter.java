package kr.kiomn2.bigtraffic.infrastructure.finance.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.kiomn2.bigtraffic.domain.finance.service.FinanceDataEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Converter
@Component
@RequiredArgsConstructor
public class CardNumberConverter implements AttributeConverter<String, String> {

    private final FinanceDataEncryptor encryptor;

    @Override
    public String convertToDatabaseColumn(String plainText) {
        return encryptor.encrypt(plainText);
    }

    @Override
    public String convertToEntityAttribute(String encrypted) {
        return encryptor.decrypt(encrypted);
    }
}
