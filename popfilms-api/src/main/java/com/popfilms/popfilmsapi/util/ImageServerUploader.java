package com.popfilms.popfilmsapi.util;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ImageServerUploader {
    private static final RestTemplate restTemplate = new RestTemplate();
    public static void sendPostRequest(String url, Resource poster_imageFile, Resource bg_imageFile) {
        LinkedMultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("poster_image_file", poster_imageFile); // Assuming MultipartFile is available
        requestBody.add("bg_image_file", bg_imageFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public static String getImageName(String originalFileName,Long movieId)
    {
        int extIndex = originalFileName.lastIndexOf('.');
        return (movieId + originalFileName.substring(extIndex));
    }
}
