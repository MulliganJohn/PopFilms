package com.popfilms.popfilmsapi.Controller;

import com.popfilms.popfilmsapi.Entity.ReviewEntity;
import com.popfilms.popfilmsapi.Service.ReviewService;
import com.popfilms.popfilmsapi.Service.UserService;
import com.popfilms.popfilmsapi.dtos.MovieReviewResponseDto;
import com.popfilms.popfilmsapi.dtos.ReviewCheckDto;
import com.popfilms.popfilmsapi.dtos.ReviewRequestDto;
import com.popfilms.popfilmsapi.dtos.UserReviewResponseDto;
import com.popfilms.popfilmsapi.util.Exceptions.InvalidReviewException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @PostMapping(value = "/api/reviews/addreview")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequestDto reviewRequestDto, @CookieValue(name = "popfilms_auth") String authToken){
        try{
            if (reviewService.addReview(reviewRequestDto, authToken)){
                return ResponseEntity.status(HttpStatus.OK).body("Review Successfully Added!");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Adding Review!");
            }
        }
        catch (InvalidReviewException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Adding Review!");
        } catch (NoSuchAlgorithmException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Adding Review!");
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @DeleteMapping(value = "/api/reviews/removereview")
    public ResponseEntity<String> removeReview(@RequestParam Long id, @CookieValue(name = "popfilms_auth") String authToken){
        try{
            if (reviewService.removeReview(id, authToken)){
                return ResponseEntity.status(HttpStatus.OK).body("Review Removed Successfully!");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Removing Review!");
            }
        }
        catch (NoSuchAlgorithmException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Removing Review!");
        }
        catch (BadCredentialsException e){
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @PutMapping(value = "/api/reviews/editreview")
    public ResponseEntity<String> editReview(@RequestBody ReviewRequestDto reviewRequestDto, @RequestParam Long id, @CookieValue(name = "popfilms_auth") String authToken){
        try{
            if (reviewService.editReview(id, authToken, reviewRequestDto)){
                return ResponseEntity.status(HttpStatus.OK).body("Review Edited Successfully!");
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Editing Review!");
            }
        }
        catch (NoSuchAlgorithmException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Editing Review!");
        }
        catch (BadCredentialsException e){
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @GetMapping("/api/reviews/getMovieReviews/{id}")
    public ResponseEntity<List<MovieReviewResponseDto>> getMovieReviewsById(@PathVariable Long id) {
        List<MovieReviewResponseDto> movieReviews = reviewService.getMovieReviewsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(movieReviews);
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @GetMapping("/api/reviews/getUserReviews/{id}")
    public ResponseEntity<List<UserReviewResponseDto>> getUserReviewsById(@PathVariable Long id) {
        List<UserReviewResponseDto> userReviews = userService.getUserReviewsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userReviews);
    }

    @CrossOrigin(origins = {"http://popfilms.xyz", "http://localhost:3000", "https://popfilms.xyz"}, allowCredentials = "true")
    @GetMapping("/api/reviews/checkUserReview/{id}")
    public ResponseEntity<ReviewCheckDto> checkUserReviewByMovieId(@PathVariable Long id, @CookieValue(name = "popfilms_auth", required = false) String authToken) {
        if (authToken == null){
            return ResponseEntity.status(HttpStatus.OK).body(ReviewCheckDto.getEmptyUserReviewCheck(false));
        }

        try{
            Optional<ReviewCheckDto> ReviewCheckDtoOptional = reviewService.checkUserReviewByMovieId(id, authToken);
            if (ReviewCheckDtoOptional.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(ReviewCheckDtoOptional.get());
            }
            else {
                return ResponseEntity.status(HttpStatus.OK).body(ReviewCheckDto.getEmptyUserReviewCheck(true));
            }
        }
        catch (NoSuchAlgorithmException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (BadCredentialsException e){
            ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(ReviewCheckDto.getEmptyUserReviewCheck(false));
        }
    }
}
