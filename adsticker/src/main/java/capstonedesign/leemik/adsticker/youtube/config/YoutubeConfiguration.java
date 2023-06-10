package capstonedesign.leemik.adsticker.youtube.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class YoutubeConfiguration {
    @Bean
    public YouTube youTube() throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory, null)
                .setApplicationName("youtube-comment-fetcher")
                .build();
    }
}
