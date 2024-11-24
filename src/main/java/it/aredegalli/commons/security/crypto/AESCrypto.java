package it.aredegalli.commons.security.crypto;

import it.aredegalli.commons.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESCrypto {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;

    private final SecurityProperties securityProperties;

    @Autowired
    public AESCrypto(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public String encrypt(String plainText) throws Exception {
        return this.encrypt(plainText, this.securityProperties.getCrypto().getAesSecretKey());
    }

    public String decrypt(String cipherText) throws Exception {
        return this.decrypt(cipherText, this.securityProperties.getCrypto().getAesSecretKey());
    }

    public String encrypt(String plainText, String encodedKey) throws Exception {
        byte[] key = Base64.getDecoder().decode(encodedKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        byte[] cipherTextWithIv = new byte[IV_SIZE + cipherText.length];
        System.arraycopy(iv, 0, cipherTextWithIv, 0, IV_SIZE);
        System.arraycopy(cipherText, 0, cipherTextWithIv, IV_SIZE, cipherText.length);

        return Base64.getEncoder().encodeToString(cipherTextWithIv);
    }

    public String decrypt(String cipherText, String encodedKey) throws Exception {
        byte[] key = Base64.getDecoder().decode(encodedKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);

        byte[] cipherTextWithIv = Base64.getDecoder().decode(cipherText);
        byte[] iv = new byte[IV_SIZE];
        byte[] actualCipherText = new byte[cipherTextWithIv.length - IV_SIZE];

        System.arraycopy(cipherTextWithIv, 0, iv, 0, IV_SIZE);
        System.arraycopy(cipherTextWithIv, IV_SIZE, actualCipherText, 0, actualCipherText.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);

        byte[] plainText = cipher.doFinal(actualCipherText);
        return new String(plainText);
    }
}
