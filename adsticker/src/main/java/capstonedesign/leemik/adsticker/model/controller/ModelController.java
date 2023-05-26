package capstonedesign.leemik.adsticker.model.controller;

import capstonedesign.leemik.adsticker.model.environment.LeemikEnvironment;
import capstonedesign.leemik.adsticker.youtube.comment.retriever.YoutubeCommentRetriever;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//@RestController

@Component
@Slf4j
public class ModelController {
    private final RestTemplate restTemplate;
    private final LeemikEnvironment env;
    private final YoutubeCommentRetriever commentRetriever;
    @Autowired
    public ModelController(RestTemplate restTemplate, LeemikEnvironment env, YoutubeCommentRetriever commentRetriever) {
        this.restTemplate = restTemplate;
        this.env = env;
        this.commentRetriever = commentRetriever;
    }

    private String processNLP(String inputData) {
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

    @Async
    public CompletableFuture<Double> callModel(String url) throws IOException {
        List<String> comments = commentRetriever.getComments(url);
        Double sumNLP = (double)0;

        for(String str : comments) {
            sumNLP += Double.parseDouble(processNLP(str));
        }

        return CompletableFuture.completedFuture(sumNLP/comments.size());
    }
}
