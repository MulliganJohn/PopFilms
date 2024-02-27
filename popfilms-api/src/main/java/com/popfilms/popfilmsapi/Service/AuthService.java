package com.popfilms.popfilmsapi.Service;

import com.popfilms.popfilmsapi.Entity.RoleEntity;
import com.popfilms.popfilmsapi.Entity.TokenEntity;
import com.popfilms.popfilmsapi.Entity.UserEntity;
import com.popfilms.popfilmsapi.Repository.RoleRepo;
import com.popfilms.popfilmsapi.Repository.TokenRepo;
import com.popfilms.popfilmsapi.Repository.UserRepo;
import com.popfilms.popfilmsapi.Security.CustomAuthenticationProviderManager;
import com.popfilms.popfilmsapi.dtos.LoginDto;
import com.popfilms.popfilmsapi.dtos.RegistrationDto;
import com.popfilms.popfilmsapi.util.Exceptions.UserRegistrationException;
import com.popfilms.popfilmsapi.util.ShaUtils;
import com.popfilms.popfilmsapi.util.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private PasswordEncoder passwordEncoder;
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private TokenUtils tokenUtils;

    private TokenRepo tokenRepo;
    private final CustomAuthenticationProviderManager customAuthenticationProviderManager;

    @Transactional
    public void registerUser(RegistrationDto registrationDto) {


        if (!isValidUsername(registrationDto.getUsername())) {
            throw new UserRegistrationException("Username Is Invalid");
        }

        if (!isValidEmail(registrationDto.getEmailAddress())) {
            throw new UserRegistrationException("Email Is Invalid");
        }

        if (!isValidPassword(registrationDto.getPassword())) {
            throw new UserRegistrationException("Password is invalid");
        }

        if (userRepo.existsByUsername(registrationDto.getUsername())) {
            throw new UserRegistrationException("Username Is Already Taken!");
        } else if (userRepo.existsByEmailAddress(registrationDto.getEmailAddress())) {
            throw new UserRegistrationException("Email Is Already Taken!");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmailAddress(registrationDto.getEmailAddress());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        RoleEntity role = roleRepo.findByRoleName("ROLE_USER").get();
        newUser.addRole(role);

        newUser.setJoinEpoch(System.currentTimeMillis());

        userRepo.save(newUser);
    }


    @Transactional
    public String tryLogin(LoginDto loginDto) throws NoSuchAlgorithmException {
        Optional<UserEntity> userEntity = userRepo.findByUsername(loginDto.getUsername());
        if (userEntity.isPresent()){
            if (passwordEncoder.matches(loginDto.getPassword(), userEntity.get().getPassword())){
                TokenEntity newToken = new TokenEntity();
                String generatedToken = tokenUtils.generateToken64();
                String encryptedToken = ShaUtils.getSha3256Hash(generatedToken);
                while(tokenRepo.existsByTokenValue(encryptedToken)){
                    generatedToken = tokenUtils.generateToken64();
                    encryptedToken = ShaUtils.getSha3256Hash(generatedToken);
                }
                newToken.setTokenValue(encryptedToken);
                tokenRepo.save(newToken);
                userEntity.get().addToken(newToken);
                return generatedToken;
            }
            throw new BadCredentialsException("Invalid Password!");
        }
        throw new BadCredentialsException("User was not found!");
    }


    public static boolean isValidUsername(String username) {
        int MIN_LENGTH = 3;
        int MAX_LENGTH = 20;

        if (username == null) {
            return false;
        }

        int length = username.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            return false;
        }

        String regex = "^[a-zA-Z0-9_]+$";
        if (!username.matches(regex)) {
            return false;
        }

        return true;
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        if (email.length() < 6 || email.length() > 320) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1 || atIndex == 0 || atIndex == email.length() - 1) {
            return false;
        }

        int lastDotIndex = email.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex < atIndex + 2 || lastDotIndex == email.length() - 1) {
            return false;
        }

        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        // Check minimum and maximum length
        if (password.length() < 8 || password.length() > 50) {
            return false;
        }

        if (!checkPasswordConstraints(password)){
            return false;
        }

        return true;
    }

    private static boolean checkPasswordConstraints(String password){
        boolean containsDigit = false;
        boolean containsUpperCase = false;
        boolean containsLowerCase = false;

        for (char c : password.toCharArray()) {
            if (!containsUpperCase && Character.isUpperCase(c)) {
                containsUpperCase = true;
            }
            if (!containsDigit && Character.isDigit(c)){
                containsDigit = true;
            }
            if (!containsLowerCase && Character.isLowerCase(c)) {
                containsLowerCase = true;
            }
            if (containsLowerCase && containsDigit && containsUpperCase){
                return true;
            }
        }
        return false;
    }

}
