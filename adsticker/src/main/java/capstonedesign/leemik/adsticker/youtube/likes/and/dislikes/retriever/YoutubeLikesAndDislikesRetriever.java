package capstonedesign.leemik.adsticker.youtube.likes.and.dislikes.retriever;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class YoutubeLikesAndDislikesRetriever {
    private final String BASE_URL = "https://returnyoutubedislikeapi.com/Votes?videoId=";
    private final RestTemplate restTemplate;

    @Autowired
    public YoutubeLikesAndDislikesRetriever(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Double getLikesAndDislikes(String videoId) throws IOException, InterruptedException {
        String url = BASE_URL + videoId;
        ResponseEntity<String> response = null;
        int retries = 3;
        int delay = 1000;

        for(int i=0;i < retries; i++) {
            try{
                response = restTemplate.getForEntity(url, String.class);
                break;
            } catch (HttpClientErrorException.TooManyRequests e) {
                log.error("Too many request, wating for " + delay + "ms before retrying...");
                Thread.sleep(delay);
            }
        }

        if(response == null) {
            return 0.5;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;

        try {
            root = mapper.readTree(response.getBody());
            Double likes = root.path("likes").asDouble();
            Double dislikes = root.path("dislikes").asDouble();

            log.info("VideoID : " + videoId + " Likes: " + likes + " Dislikes: " + dislikes + " Return Result : " + likes/(likes+ + dislikes));
            return likes / (likes + dislikes);

        }   catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return 0.5;
    }
}
