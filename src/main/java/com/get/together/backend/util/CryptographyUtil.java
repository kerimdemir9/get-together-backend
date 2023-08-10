package com.get.together.backend.util;

import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class CryptographyUtil {

    private static final String KEY = "bxc0%xbax18x02nxd9Sx8c=gx8b,xa3L";
    private final String ENCRYPTION_ALGORITHM = "AES";
    private final String TRANSFORMATION_ECB = "AES/ECB/PKCS5Padding";

    public String encrypt(String data) {
        try {
            if (StringUtils.isBlank(data)) {
                return null;
            }

            val cipher = Cipher.getInstance(TRANSFORMATION_ECB);
            val secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return new Base64().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            return data;
        }
    }

    public String decrypt(String encryptedData) {
        try {
            if (StringUtils.isBlank(encryptedData)) {
                return null;
            }

            val cipher = Cipher.getInstance(TRANSFORMATION_ECB);
            val secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return new String(cipher.doFinal(new Base64().decode(encryptedData)), StandardCharsets.UTF_8);
        } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            return encryptedData;
        }
    }
}
