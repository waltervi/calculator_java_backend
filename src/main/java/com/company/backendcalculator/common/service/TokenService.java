package com.company.backendcalculator.common.service;

import com.company.backendcalculator.common.dto.UserData;

import javax.crypto.spec.SecretKeySpec;

public interface TokenService {
    UserData decryptAndValidateToken(String token);

    String encryptToken(UserData userData);

    String encrypt(String strToEncrypt) throws Exception;

    String decrypt(String strToDecrypt) throws Exception;

    SecretKeySpec getSecretKeySpec() throws Exception;
}
