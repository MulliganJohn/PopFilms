package com.popfilms.popfilmsapi.Security;

import com.popfilms.popfilmsapi.Security.Provider.CustomTokenAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationProviderManager implements AuthenticationManager {

    private final CustomTokenAuthenticationProvider customTokenAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return customTokenAuthenticationProvider.authenticate(authentication);
    }
}
