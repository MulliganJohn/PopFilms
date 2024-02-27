package com.popfilms.popfilmsapi.Security.Provider;

import com.popfilms.popfilmsapi.Entity.TokenEntity;
import com.popfilms.popfilmsapi.Entity.UserEntity;
import com.popfilms.popfilmsapi.Repository.TokenRepo;
import com.popfilms.popfilmsapi.Repository.UserRepo;
import com.popfilms.popfilmsapi.Security.Authentication.CustomTokenAuthentication;
import com.popfilms.popfilmsapi.util.ShaUtils;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomTokenAuthenticationProvider implements AuthenticationProvider {

    private final TokenRepo tokenRepo;

    private final UserRepo userRepo;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomTokenAuthentication customTokenAuthentication = (CustomTokenAuthentication) authentication;
        String authToken = customTokenAuthentication.getKey();
        if (authToken == null){
            //throw new BadCredentialsException("Invalid authentication token!");
            return new CustomTokenAuthentication(false, authToken, new HashSet<>());
        }
        try {
            String hashedAuthToken = ShaUtils.getSha3256Hash(authToken);
            Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(hashedAuthToken);
            if (tokenEntityOptional.isPresent()){
                TokenEntity tokenEntity = tokenEntityOptional.get();
                UserEntity userEntity = tokenEntity.getUser();
                return new CustomTokenAuthentication(true, authToken, userEntity.getAuthorities());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //throw new BadCredentialsException("Invalid authentication token!");
        return new CustomTokenAuthentication(false, authToken, new HashSet<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomTokenAuthentication.class.equals(authentication);
    }
}
