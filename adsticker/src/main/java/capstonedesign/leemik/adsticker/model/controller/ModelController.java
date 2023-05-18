package capstonedesign.leemik.adsticker.model.controller;

import capstonedesign.leemik.adsticker.model.environment.LeemikEnvironment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RestController

@Component
@Slf4j
public class ModelController {
    private final RestTemplate restTemplate;
    private final LeemikEnvironment env;

    public ModelController(RestTemplate restTemplate, LeemikEnvironment env) {
        this.restTemplate = restTemplate;
        this.env = env;
    }

    public String callModel(String inputData) {
        ObjectMapper objectMapper = new ObjectMapper();
        String modelUrl = env.getHost() + ":" + env.getPort();
        String jsonInputData;

        try {
            jsonInputData = objectMapper.writeValueAsString(inputData);
        } catch (Exception e) {
            throw new RuntimeException("Error converting inputData to JSON", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonInputData, headers);

        return restTemplate.postForObject(modelUrl, entity, String.class);
    }
}
