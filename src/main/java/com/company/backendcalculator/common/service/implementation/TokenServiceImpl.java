package com.company.backendcalculator.common.service.implementation;

import com.company.backendcalculator.common.service.TokenService;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.exceptions.Http401Exception;
import com.company.backendcalculator.common.exceptions.Http500Exception;
import com.company.backendcalculator.common.service.ObjectMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;

@Service
public class TokenServiceImpl implements TokenService {
    @Value("${token.secret}")
    private String secret;

    @Value("${token.salt}")
    private String salt;

    @Value("${token.key}")
    private String tokenKey;

    @Autowired
    private ObjectMapperService objectMapperService;

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";


    @Override
    public UserData decryptAndValidateToken(String token) {
        UserData userData = null;
        try {
            String decrypted = decrypt(token);
            userData = objectMapperService.getObjectMapper().readValue(decrypted, UserData.class);

            if(!tokenKey.equals(userData.getTokenKey())){
                throw new Http401Exception();
            }

        } catch (Exception ex) {
            throw new Http401Exception();
        }
        LocalDate date = Instant.ofEpochMilli(userData.getEndTimeStamp()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (date.isBefore(LocalDate.now())) {
            throw new Http400Exception("user must relogin");
        }

        return userData;
    }

    @Override
    public String encryptToken(UserData userData) {
        String encrypted = null;
        try {
            String json = objectMapperService.getObjectMapper().writeValueAsString(userData);
            encrypted = encrypt(json);
        } catch (Exception ex) {
            throw new Http500Exception("error while encrypting token");
        }
        return encrypted;
    }

    @Override
    public String encrypt(String strToEncrypt) throws Exception {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKey secretKey = getSecretKeySpec();


        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return Base64.getEncoder()
                .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public String decrypt(String strToDecrypt) throws Exception {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKey secretKey = getSecretKeySpec();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

    }

    private static SecretKeySpec secretKeySpec;

    @Override
    public SecretKeySpec getSecretKeySpec() throws Exception{
        if( secretKeySpec == null){
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 1, 256);
            SecretKey secretKey = factory.generateSecret(spec);
            secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        }
        return secretKeySpec;
    }

}
