package com.popfilms.popfilmsapi.Controller;

import com.popfilms.popfilmsapi.Entity.TokenEntity;
import com.popfilms.popfilmsapi.Entity.UserEntity;
import com.popfilms.popfilmsapi.Repository.UserRepo;
import com.popfilms.popfilmsapi.Service.AuthService;
import com.popfilms.popfilmsapi.dtos.LoginDto;
import com.popfilms.popfilmsapi.dtos.RegistrationDto;
import com.popfilms.popfilmsapi.util.Exceptions.UserRegistrationException;
import com.popfilms.popfilmsapi.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"})
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDto registrationDto){
        try {
            authService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.OK).body("User Successfully Registered!");
        }
        catch (UserRegistrationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        try{
            String authToken = authService.tryLogin(loginDto);
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", authToken)
                    .httpOnly(true)  // Optional: Make the cookie accessible only via HTTP (not JavaScript)
                    .path("/")       // Optional: Set the cookie path
                    .maxAge(Duration.ofDays(365))
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("User Successfully Logged In!");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
