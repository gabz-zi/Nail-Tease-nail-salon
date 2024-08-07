package com.nailSalon.service;



import com.nailSalon.model.dto.DesignDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class DesignServiceClient {

    private final RestTemplate restTemplate;

    @Value("${new.app.api.url}")
    private String newAppApiUrl;

    public DesignServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<DesignDTO> getAllDesigns() {
        ResponseEntity<DesignDTO[]> response = restTemplate.getForEntity(newAppApiUrl, DesignDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public void deleteDesign(Long id) {
        restTemplate.delete(newAppApiUrl + "/" + id);
    }

    public void createDesign(String name, String category, MultipartFile image, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Prepare multipart data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", name);
        body.add("category", category);
        body.add("image", new MultipartFileResource(image));
        body.add("madeBy", username);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(newAppApiUrl, requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            // Log or handle the error
            System.err.println("Error creating design: " + e.getResponseBodyAsString());
        }
    }

    // Helper class to convert MultipartFile to Resource
    private static class MultipartFileResource extends FileSystemResource {
        private final MultipartFile multipartFile;

        public MultipartFileResource(MultipartFile multipartFile) {
            super(multipartFile.getOriginalFilename());
            this.multipartFile = multipartFile;
        }

        @Override
        public long contentLength() throws IOException {
            return multipartFile.getSize();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return multipartFile.getInputStream();
        }
    }
}

