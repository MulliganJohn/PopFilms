package com.popfilms.popfilmsapi.Service;

import com.popfilms.popfilmsapi.Entity.*;
import com.popfilms.popfilmsapi.Repository.MovieRepo;
import com.popfilms.popfilmsapi.Repository.ReviewRepo;
import com.popfilms.popfilmsapi.Repository.TokenRepo;
import com.popfilms.popfilmsapi.Repository.UserRepo;
import com.popfilms.popfilmsapi.dtos.MovieReviewResponseDto;
import com.popfilms.popfilmsapi.dtos.ReviewCheckDto;
import com.popfilms.popfilmsapi.dtos.ReviewRequestDto;
import com.popfilms.popfilmsapi.dtos.UserReviewResponseDto;
import com.popfilms.popfilmsapi.util.Exceptions.InvalidReviewException;
import com.popfilms.popfilmsapi.util.ShaUtils;
import com.popfilms.popfilmsapi.util.TokenUtils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService {

    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    MovieRepo movieRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    ReviewRepo reviewRepo;

    @Transactional
    public boolean addReview(ReviewRequestDto reviewRequestDto, String authToken) throws NoSuchAlgorithmException {
        try {
            Optional<ReviewCheckDto> reviewCheckDtoOptional = checkUserReviewByMovieId(reviewRequestDto.getMovieId(), authToken);
            if (reviewCheckDtoOptional.isPresent()){
                throw new InvalidReviewException("User already has a review for this movie!");
            }
            ReviewEntity reviewEntity = parseEntityFromDTO(reviewRequestDto, authToken);
            reviewRepo.save(reviewEntity);
            return true;
        }
        catch (InvalidReviewException e){
            throw new InvalidReviewException(e.getMessage());
        } catch (RuntimeException e){
            throw new RuntimeException(e.toString());
        } catch (NoSuchAlgorithmException e){
            throw new NoSuchAlgorithmException(e.toString());
        }
    }

    public boolean removeReview(Long id, String authToken) throws NoSuchAlgorithmException {
        Optional<ReviewEntity> reviewEntityOptional = reviewRepo.findById(id);
        if (!reviewEntityOptional.isPresent()){
            return false;
        }
        ReviewEntity reviewEntity = reviewEntityOptional.get();
        Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(ShaUtils.getSha3256Hash(authToken));
        if (tokenEntityOptional.isPresent()){
            TokenEntity tokenEntity = tokenEntityOptional.get();
            UserEntity userEntity = tokenEntity.getUser();
            Set<RoleEntity> userRoles = userEntity.getRoles();
            if(userRoles.stream().anyMatch(role -> "ROLE_MOD".equals(role.getRoleName()) || "ROLE_ADMIN".equals(role.getRoleName()))){
                reviewRepo.delete(reviewEntity);
                return true;
            }
            else if (reviewEntity.getUser().equals(userEntity)){
                reviewRepo.delete(reviewEntity);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            throw new BadCredentialsException("User was not found!");
        }
    }

    public boolean editReview(Long id, String authToken, ReviewRequestDto reviewRequestDto) throws NoSuchAlgorithmException {
        Optional<ReviewEntity> reviewEntityOptional = reviewRepo.findById(id);
        if (!reviewEntityOptional.isPresent()){
            return false;
        }
        ReviewEntity reviewEntity = reviewEntityOptional.get();
        Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(ShaUtils.getSha3256Hash(authToken));
        if (tokenEntityOptional.isPresent()){
            TokenEntity tokenEntity = tokenEntityOptional.get();
            UserEntity userEntity = tokenEntity.getUser();
            if (reviewEntity.getUser().equals(userEntity) && reviewEntity.getMovie().getId().equals(reviewRequestDto.getMovieId())){
                try {
                    ReviewEntity newReviewEntity = parseEntityFromDTO(reviewRequestDto, authToken);
                    reviewEntity.setReviewRating(newReviewEntity.getReviewRating());
                    reviewEntity.setReviewBody(newReviewEntity.getReviewBody());
                    reviewEntity.setReviewTitle(newReviewEntity.getReviewTitle());
                    reviewRepo.save(reviewEntity);
                    return true;
                }
                catch (InvalidReviewException e){
                    return false;
                } catch (RuntimeException e){
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            throw new BadCredentialsException("User was not found!");
        }
    }



    public List<MovieReviewResponseDto> getMovieReviewsById(Long id){
        List<MovieReviewResponseDto> movieReviews = new ArrayList<>();
        Optional<MovieEntity> movieEntityOptional = movieRepo.findById(id);
        if (movieEntityOptional.isPresent()){
            for (ReviewEntity reviewEntity : movieEntityOptional.get().getReviews()){
                MovieReviewResponseDto movieReviewResponseDto = new MovieReviewResponseDto();
                movieReviewResponseDto.setRating(reviewEntity.getReviewRating());
                movieReviewResponseDto.setBody(reviewEntity.getReviewBody());
                movieReviewResponseDto.setEpoch(reviewEntity.getReviewCreationEpoch());
                movieReviewResponseDto.setTitle(reviewEntity.getReviewTitle());
                movieReviewResponseDto.setReviewId(reviewEntity.getReviewId());
                UserEntity reviewUser = reviewEntity.getUser();
                movieReviewResponseDto.setUser(reviewUser.getUserId(), reviewUser.getUsername(), reviewUser.getReviews().size());
                movieReviews.add(movieReviewResponseDto);
            }
        }
        return movieReviews;
    }

    public ReviewEntity parseEntityFromDTO(ReviewRequestDto reviewRequestDto, String authToken){
        ReviewEntity reviewEntity = new ReviewEntity();
        if (reviewRequestDto.getTitle() != null && reviewRequestDto.getTitle().length() > 5){
            reviewEntity.setReviewTitle(reviewRequestDto.getTitle());
        }
        else {
            throw new InvalidReviewException("Review title was invalid!");
        }
        if (reviewRequestDto.getBody() != null && reviewRequestDto.getBody().length() > 20){
            reviewEntity.setReviewBody(reviewRequestDto.getBody());
        }
        else {
            throw new InvalidReviewException("Review body was invalid!");
        }
        if (reviewRequestDto.getRating() != null && reviewRequestDto.getRating() >= 0 && reviewRequestDto.getRating() <= 10){
            reviewEntity.setReviewRating(reviewRequestDto.getRating());
        }
        else{
            throw new InvalidReviewException("Review rating was invalid!");
        }

        Optional<MovieEntity> movieEntityOptional = movieRepo.findById(reviewRequestDto.getMovieId());
        if (movieEntityOptional.isPresent()) reviewEntity.setMovie(movieEntityOptional.get());
        else throw new InvalidReviewException("Review movie ID was invalid!");

        try{
            Optional<UserEntity> userEntityOptional = tokenUtils.getUserByToken(authToken);
            if (userEntityOptional.isPresent()) reviewEntity.setUser(userEntityOptional.get());
            else throw new InvalidReviewException("Review user ID was invalid!");
        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException();
        }

        reviewEntity.setReviewCreationEpoch(System.currentTimeMillis());
        return reviewEntity;
    }

    public Optional<ReviewCheckDto> checkUserReviewByMovieId(Long id, String authToken) throws NoSuchAlgorithmException{
        Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(ShaUtils.getSha3256Hash(authToken));
        if (tokenEntityOptional.isPresent()){
            TokenEntity tokenEntity = tokenEntityOptional.get();
            UserEntity userEntity = tokenEntity.getUser();
            Optional<ReviewEntity> reviewEntityOptional = userEntity.findReviewByMovieId(id);
            if (reviewEntityOptional.isPresent()){
                ReviewEntity reviewEntity = reviewEntityOptional.get();
                ReviewCheckDto reviewCheckDto = new ReviewCheckDto();
                UserReviewResponseDto userReviewResponseDto = new UserReviewResponseDto();
                reviewCheckDto.setRating(reviewEntity.getReviewRating());
                reviewCheckDto.setBody(reviewEntity.getReviewBody());
                reviewCheckDto.setEpoch(reviewEntity.getReviewCreationEpoch());
                reviewCheckDto.setTitle(reviewEntity.getReviewTitle());
                reviewCheckDto.setMovieId(reviewEntity.getMovie().getId());
                reviewCheckDto.setReviewId(reviewEntity.getReviewId());
                reviewCheckDto.setDoesReviewExist(true);
                reviewCheckDto.setUserRecognized(true);
                return Optional.of(reviewCheckDto);
            }
            else {
                return Optional.empty();
            }
        }
        else {
            throw new BadCredentialsException("User was not found!");
        }
    }

}
