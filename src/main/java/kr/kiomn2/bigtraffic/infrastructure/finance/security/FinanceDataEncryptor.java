package kr.kiomn2.bigtraffic.infrastructure.finance.security;

import kr.kiomn2.bigtraffic.infrastructure.finance.config.EncryptionConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

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
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }

        try {
            SecretKey secretKey = encryptionConfig.getSecretKey();

            byte[] decodedData = Base64.getDecoder().decode(encryptedText);

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
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
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
