package kr.kiomn2.bigtraffic.infrastructure.finance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
public class EncryptionConfig {

    @Value("${finance.encryption.secret-key:}")
    private String secretKeyString;

    public SecretKey getSecretKey() {
        if (secretKeyString == null || secretKeyString.isEmpty()) {
            // 개발 환경용 기본 키 생성 (프로덕션에서는 반드시 환경변수 설정 필요)
            return generateDefaultKey();
        }

        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    private SecretKey generateDefaultKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES algorithm not available", e);
        }
    }
}
