package kr.kiomn2.bigtraffic.infrastructure.finance.security;

import kr.kiomn2.bigtraffic.infrastructure.finance.config.EncryptionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceDataEncryptor {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 12 bytes
    private static final int GCM_TAG_LENGTH = 128; // 128 bits

    private final EncryptionConfig encryptionConfig;

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }

        try {
            SecretKey secretKey = encryptionConfig.getSecretKey();

            // IV 생성
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            // 암호화
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedData = cipher.doFinal(plainText.getBytes());

            // IV와 암호화된 데이터 결합
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
            byteBuffer.put(iv);
            byteBuffer.put(encryptedData);

            return Base64.getEncoder().encodeToString(byteBuffer.array());

        } catch (Exception e) {
            log.error("=== 암호화 실패 ===");
            log.error("Exception Type: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage());
            log.error("입력 데이터 길이: {}", plainText.length());
            log.error("StackTrace: ", e);
            log.error("===================");
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }

        try {
            // Base64 디코딩 시도 - 실패하면 평문일 가능성이 높음
            byte[] decodedData;
            try {
                decodedData = Base64.getDecoder().decode(encryptedText);
            } catch (IllegalArgumentException e) {
                log.warn("Base64 디코딩 실패 - 평문 데이터로 간주합니다. Text length: {}", encryptedText.length());
                return encryptedText; // 평문 그대로 반환
            }

            // 데이터 길이 검증 (IV + 최소 암호화 데이터)
            if (decodedData.length < GCM_IV_LENGTH + 16) { // 16 = GCM tag 최소 크기
                log.warn("암호화 데이터가 너무 짧습니다. 평문으로 간주합니다. Length: {}", decodedData.length);
                return encryptedText;
            }

            SecretKey secretKey = encryptionConfig.getSecretKey();

            // IV와 암호화된 데이터 분리
            ByteBuffer byteBuffer = ByteBuffer.wrap(decodedData);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);
            byte[] encryptedData = new byte[byteBuffer.remaining()];
            byteBuffer.get(encryptedData);

            // 복호화
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] decryptedData = cipher.doFinal(encryptedData);
            return new String(decryptedData);

        } catch (javax.crypto.AEADBadTagException e) {
            log.error("=== 복호화 실패: 잘못된 암호화 키 또는 손상된 데이터 ===");
            log.error("입력 데이터 길이: {}", encryptedText.length());
            log.error("입력 데이터 첫 50자: {}", encryptedText.substring(0, Math.min(50, encryptedText.length())));
            log.error("AEADBadTagException - 암호화 키가 다르거나 데이터가 손상되었습니다.");
            log.error("StackTrace: ", e);
            log.error("==========================================================");

            // 평문으로 간주하고 반환 (마이그레이션 지원)
            log.warn("복호화 실패한 데이터를 평문으로 반환합니다.");
            return encryptedText;

        } catch (Exception e) {
            log.error("=== 복호화 실패: 예상치 못한 오류 ===");
            log.error("Exception Type: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage());
            log.error("입력 데이터 길이: {}", encryptedText.length());
            log.error("입력 데이터 첫 50자: {}", encryptedText.substring(0, Math.min(50, encryptedText.length())));
            log.error("StackTrace: ", e);
            log.error("=========================================");

            // 복호화 실패 시 평문으로 간주 (하위 호환성)
            log.warn("복호화 실패한 데이터를 평문으로 반환합니다.");
            return encryptedText;
        }
    }

    public String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return accountNumber;
        }

        String cleaned = accountNumber.replaceAll("[^0-9]", "");
        if (cleaned.length() < 7) {
            return "***-" + cleaned.substring(cleaned.length() - 4);
        }

        String first = cleaned.substring(0, 3);
        String last = cleaned.substring(cleaned.length() - 4);
        return first + "-***-***" + last;
    }

    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }

        String cleaned = cardNumber.replaceAll("[^0-9]", "");
        String last = cleaned.substring(cleaned.length() - 4);
        return "****-****-****-" + last;
    }

    public String extractLastFourDigits(String number) {
        if (number == null || number.isEmpty()) {
            return "";
        }

        String cleaned = number.replaceAll("[^0-9]", "");
        if (cleaned.length() < 4) {
            return cleaned;
        }

        return cleaned.substring(cleaned.length() - 4);
    }
}
