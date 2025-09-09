package project.borrowhen.common.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CipherUtil {

    private final String algorithm;
    private final String secret;

    public CipherUtil(
            @Value("${cipher.algorithm}") String algorithm,
            @Value("${cipher.key}") String secret) {
        this.algorithm = algorithm;
        this.secret = secret;
    }

    public String encrypt(String value) throws Exception {
        Key key = generateKey(secret);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedByteValue);
    }

    public String decrypt(String value) throws Exception {
        value = value.replaceAll("\\s+", "+");
        Key key = generateKey(secret);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.getUrlDecoder().decode(value);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue, StandardCharsets.UTF_8);
    }

    private Key generateKey(String secretKey) {
        return new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algorithm);
    }
}
