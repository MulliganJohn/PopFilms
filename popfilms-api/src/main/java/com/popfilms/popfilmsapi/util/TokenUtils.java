package com.popfilms.popfilmsapi.util;

import com.popfilms.popfilmsapi.Entity.TokenEntity;
import com.popfilms.popfilmsapi.Entity.UserEntity;
import com.popfilms.popfilmsapi.Repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

@Component
public class TokenUtils {
    static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String lower = upper.toLowerCase();
    static final String digits = "0123456789";
    static final String symbols = upper + lower + digits;

    @Autowired
    TokenRepo tokenRepo;

    public static String generateToken64() {
        int rightLimit = symbols.length();
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder buffer = new StringBuilder(64);

        for (int i = 0; i < 64; i++) {
            int randomLimitedInt = secureRandom.nextInt(rightLimit);
            buffer.append(symbols.charAt(randomLimitedInt));
        }

        return buffer.toString();
    }

    public Optional<UserEntity> getUserByToken(String tokenValue) throws NoSuchAlgorithmException {
        Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(ShaUtils.getSha3256Hash(tokenValue));
        if (tokenEntityOptional.isPresent()){
            TokenEntity tokenEntity = tokenEntityOptional.get();
            UserEntity userEntity = tokenEntity.getUser();
            return Optional.ofNullable(userEntity);
        }
        else {
            return Optional.empty();
        }
    }
}
