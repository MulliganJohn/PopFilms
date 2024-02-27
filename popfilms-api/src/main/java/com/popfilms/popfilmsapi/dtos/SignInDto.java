package com.popfilms.popfilmsapi.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInDto {

    private String username;

    private String email;

    private Long userId;

    private boolean isUserRecognized;

    private boolean canModerate;

    public static SignInDto getEmptyUserSignIn(boolean userRecognized){
        SignInDto signInDto = new SignInDto();
        signInDto.isUserRecognized = userRecognized;
        return signInDto;
    }

}
