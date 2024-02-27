package com.popfilms.popfilmsapi.Service;

import com.popfilms.popfilmsapi.Entity.ReviewEntity;
import com.popfilms.popfilmsapi.Entity.RoleEntity;
import com.popfilms.popfilmsapi.Entity.TokenEntity;
import com.popfilms.popfilmsapi.Entity.UserEntity;
import com.popfilms.popfilmsapi.Repository.TokenRepo;
import com.popfilms.popfilmsapi.Repository.UserRepo;
import com.popfilms.popfilmsapi.dtos.SignInDto;
import com.popfilms.popfilmsapi.dtos.UserPageDto;
import com.popfilms.popfilmsapi.dtos.UserReviewResponseDto;
import com.popfilms.popfilmsapi.util.ShaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    TokenRepo tokenRepo;

    public SignInDto getSignInDto(String authToken) throws NoSuchAlgorithmException {
        Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(ShaUtils.getSha3256Hash(authToken));
        if (tokenEntityOptional.isPresent()){
            TokenEntity tokenEntity = tokenEntityOptional.get();
            UserEntity userEntity = tokenEntity.getUser();
            SignInDto signInDto = new SignInDto();
            signInDto.setUsername(userEntity.getUsername());
            signInDto.setEmail(userEntity.getEmailAddress());
            signInDto.setUserId(userEntity.getUserId());
            signInDto.setUserRecognized(true);
            Set<RoleEntity> userRoles = userEntity.getRoles();
            if(userRoles.stream().anyMatch(role -> "ROLE_MOD".equals(role.getRoleName()) || "ROLE_ADMIN".equals(role.getRoleName()))){
                signInDto.setCanModerate(true);
            }
            else {
                signInDto.setCanModerate(false);
            }
            return signInDto;
        }
        else{
            throw new BadCredentialsException("User Not Found");
        }
    }

    @Transactional
    public void signOut(String authToken) throws NoSuchAlgorithmException {
        Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(ShaUtils.getSha3256Hash(authToken));
        if (tokenEntityOptional.isPresent()){
            tokenRepo.deleteByTokenValue(ShaUtils.getSha3256Hash(authToken));
        }
        else{
            throw new BadCredentialsException("User Not Found");
        }
    }

    @Transactional
    public void signOutAll(String authToken) throws NoSuchAlgorithmException {
        Optional<TokenEntity> tokenEntityOptional = tokenRepo.findByTokenValue(ShaUtils.getSha3256Hash(authToken));
        if (tokenEntityOptional.isPresent())
        {
            UserEntity userEntity = tokenEntityOptional.get().getUser();
            userEntity.removeAllTokens();
            userRepo.save(userEntity);
        }
        else{
            throw new BadCredentialsException("User Not Found");
        }
    }

    public UserPageDto getUserHeaderDto(Long id){
        Optional<UserEntity> userEntityOptional = userRepo.findById(id);
        if (userEntityOptional.isPresent()){
            UserEntity userEntity = userEntityOptional.get();
            List<UserReviewResponseDto> userReviews = getUserReviewsById(id);
            UserPageDto userPageDto = new UserPageDto();
            userPageDto.setReviews(userReviews);
            userPageDto.setUserHeader(userEntity.getUsername(), userEntity.getJoinEpoch(), userReviews.size());
            return userPageDto;
        }
        else {
            return null;
        }
    }

    public List<UserReviewResponseDto> getUserReviewsById(Long id){
        List<UserReviewResponseDto> userReviews = new ArrayList<>();
        Optional<UserEntity> userEntityOptional = userRepo.findById(id);
        if (userEntityOptional.isPresent()){
            for (ReviewEntity reviewEntity : userEntityOptional.get().getReviews()){
                UserReviewResponseDto userReviewResponseDto = new UserReviewResponseDto();
                userReviewResponseDto.setRating(reviewEntity.getReviewRating());
                userReviewResponseDto.setBody(reviewEntity.getReviewBody());
                userReviewResponseDto.setEpoch(reviewEntity.getReviewCreationEpoch());
                userReviewResponseDto.setTitle(reviewEntity.getReviewTitle());
                userReviewResponseDto.setMovieId(reviewEntity.getMovie().getId());
                userReviewResponseDto.setMovieTitle(reviewEntity.getMovie().getTitle());
                userReviewResponseDto.setReviewId(reviewEntity.getReviewId());
                userReviews.add(userReviewResponseDto);
            }
        }
        return userReviews;
    }

}
