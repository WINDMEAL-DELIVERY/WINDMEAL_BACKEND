package com.windmeal.global.util;

import com.windmeal.global.exception.AesException;
import com.windmeal.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class AES256Util {

  @Value("${encrypt.alg}")
  private String alg;

  @Value("${encrypt.key}")
  private String encryptKey;

  @Value("${encrypt.iv}")
  private String iv;

  @Value("${encrypt.key_front}")
  private String key_front;

  @Value("${encrypt.iv_front}")
  private String iv_front;


  public String encrypt(String text) throws AesException {
    try {
      Cipher cipher = Cipher.getInstance(alg);
      SecretKeySpec keySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
      IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
      byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
      // 암호화가 완료된 값에 대해서 base64로 인코딩을 해준다.
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      // 암/복호화 과정에서 발생하는 모든 예외를 AesException으로 치환하여 상위 클래스로 전달한다.
      throw new AesException(ErrorCode.ENCRYPT_ERROR);
    }
  }

  public String decrypt(String cipherText) throws AesException {
    try {
      Cipher cipher = Cipher.getInstance(alg);
      SecretKeySpec keySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
      IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
      // 복호화하기 전에 디코딩해준다.
      byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
      byte[] decrypted = cipher.doFinal(decodedBytes);
      return new String(decrypted, "UTF-8");
    } catch (Exception e) {
      throw new AesException(ErrorCode.ENCRYPT_ERROR);
    }
  }

  public String doubleDecrypt(String cipherText) throws AesException {
    try {
      Cipher cipher = Cipher.getInstance(alg);
      // 프론트엔드 키를 먼저 복호화해줌
      SecretKeySpec keySpec = new SecretKeySpec(key_front.getBytes(), "AES");
      IvParameterSpec ivParamSpec = new IvParameterSpec(iv_front.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
      // 복호화하기 전에 디코딩해준다.
      byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
      byte[] decrypted = cipher.doFinal(decodedBytes);
      String tempDecryptedString = new String(decrypted, "UTF-8");
      // 이어서 백엔드 키로 다시 초기화 해준다.
      keySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
      ivParamSpec = new IvParameterSpec(iv.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
      decodedBytes = Base64.getDecoder().decode(tempDecryptedString);
      decrypted = cipher.doFinal(decodedBytes);
      return new String(decrypted, "UTF-8");
    } catch (Exception e) {
      e.printStackTrace();
      throw new AesException(ErrorCode.ENCRYPT_ERROR);
    }
  }

}
