package com.popfilms.popfilmsapi.Controller;

import com.popfilms.popfilmsapi.Service.UserService;
import com.popfilms.popfilmsapi.dtos.ReviewCheckDto;
import com.popfilms.popfilmsapi.dtos.SignInDto;
import com.popfilms.popfilmsapi.dtos.UserPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @PostMapping(value = "/api/users/signin", produces = "application/json")
    public ResponseEntity<SignInDto> signIn(@CookieValue(name = "popfilms_auth", required = false) String authToken){
        if (authToken == null){
            return ResponseEntity.status(HttpStatus.OK).body(SignInDto.getEmptyUserSignIn(false));
        }
        try{
            SignInDto signInDto = userService.getSignInDto(authToken);
            return ResponseEntity.status(HttpStatus.OK).body(signInDto);
        }
        catch (BadCredentialsException e){
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(SignInDto.getEmptyUserSignIn(false));
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @PostMapping(value = "/api/users/signout")
    public ResponseEntity<String> signOut(@CookieValue(name = "popfilms_auth") String authToken) {
        try{
            userService.signOut(authToken);
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)  // Set the maxAge to 0 to make the cookie expire immediately
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("User Successfully Signed Out");
        }
        catch (BadCredentialsException e){
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @PostMapping(value = "/api/users/signoutall")
    public ResponseEntity<String> signOutOfAllAccounts(@CookieValue(name = "popfilms_auth") String authToken){
        try{
            userService.signOutAll(authToken);
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)  // Set the maxAge to 0 to make the cookie expire immediately
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body("Successfully Signed Out Of All Locations!");
        }
        catch (BadCredentialsException e){
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @GetMapping(value = "/api/users/getuserpage")
    public ResponseEntity<UserPageDto> getUserPage(@RequestParam Long id) {
        UserPageDto userPageDto = userService.getUserHeaderDto(id);
        if (userPageDto != null){
            return ResponseEntity.status(HttpStatus.OK).body(userPageDto);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    

}
