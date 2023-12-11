package com.windmeal.global.util;

import com.windmeal.global.exception.AesException;
import com.windmeal.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AES256Util {

    @Value("${encrypt.alg}")
    private String alg;

    @Value("${encrypt.key}")
    private String encryptKey;

    @Value("${encrypt.iv}")
    private String iv;

    public String encrypt(String text) throws AesException {
        try{
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
            // 암호화가 완료된 값에 대해서 base64로 인코딩을 해준다.
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            // 예외를 AesException으로 치환한 뒤 호출한 부분에서 처리하기 위해서 로직을 구성하였다.
            throw new AesException(ErrorCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    public String decrypt(String cipherText) throws AesException {
        try{
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
            // 복호화하기 전에 디코딩해준다.
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            throw new AesException(ErrorCode.INTERNAL_ERROR, e.getMessage());
        }
    }
}
