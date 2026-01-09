package kr.kiomn2.bigtraffic.infrastructure.finance.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Configuration
public class EncryptionConfig {

    @Value("${finance.encryption.secret-key:}")
    private String secretKeyString;

    // 개발 환경용 고정 기본 키 (Base64로 인코딩된 32바이트 AES-256 키)
    // 프로덕션에서는 반드시 환경변수로 다른 키를 설정해야 합니다!
    private static final String DEFAULT_KEY_BASE64 = "YmlnVHJhZmZpY0RldmVsb3BtZW50S2V5MTIzNDU2Nzg="; // "bigTrafficDevelopmentKey12345678" 을 Base64 인코딩

    private SecretKey cachedSecretKey = null;

    public SecretKey getSecretKey() {
        if (cachedSecretKey != null) {
            return cachedSecretKey;
        }

        String keyToUse;
        if (secretKeyString == null || secretKeyString.isEmpty()) {
            log.warn("=== 경고: 암호화 키가 설정되지 않았습니다 ===");
            log.warn("개발 환경용 기본 키를 사용합니다.");
            log.warn("프로덕션 환경에서는 반드시 'finance.encryption.secret-key' 환경변수를 설정하세요!");
            log.warn("========================================");
            keyToUse = DEFAULT_KEY_BASE64;
        } else {
            log.info("설정된 암호화 키를 사용합니다.");
            keyToUse = secretKeyString;
        }

        try {
            byte[] decodedKey = Base64.getDecoder().decode(keyToUse);

            // AES-256을 위해 32바이트 키 필요
            if (decodedKey.length != 32) {
                log.warn("암호화 키 길이가 32바이트가 아닙니다. 길이: {} bytes", decodedKey.length);
                // 32바이트로 패딩 또는 자르기
                byte[] adjustedKey = new byte[32];
                System.arraycopy(decodedKey, 0, adjustedKey, 0, Math.min(decodedKey.length, 32));
                decodedKey = adjustedKey;
            }

            cachedSecretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            log.info("암호화 키 초기화 완료");
            return cachedSecretKey;

        } catch (IllegalArgumentException e) {
            log.error("=== 암호화 키 디코딩 실패 ===");
            log.error("Base64 디코딩 오류. 키가 올바른 Base64 형식인지 확인하세요.");
            log.error("StackTrace: ", e);
            log.error("============================");
            throw new RuntimeException("Failed to decode encryption key", e);
        }
    }
}
