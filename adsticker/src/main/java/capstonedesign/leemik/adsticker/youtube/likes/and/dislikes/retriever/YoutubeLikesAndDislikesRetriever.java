package capstonedesign.leemik.adsticker.youtube.likes.and.dislikes.retriever;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Component
public class YoutubeLikesAndDislikesRetriever {
    private final YouTube youTube;
    private static final String API_KEY = "AIzaSyBg_339Pp9ssNABBFoc1VScZ0BIPvgR3B8";

    @Autowired
    public YoutubeLikesAndDislikesRetriever(YouTube youTube) {
        this.youTube = youTube;
    }

    @Async
    public CompletableFuture<Double> getLikesAndDislikes(String videoId) throws IOException {
        YouTube.Videos.List request = youTube.videos()
                .list("statistics")
                .setId(videoId)
                .setKey(API_KEY);

        Video response = request.execute().getItems().get(0);
        BigInteger likeCount = response.getStatistics().getLikeCount();
        BigInteger dislikeCount = response.getStatistics().getDislikeCount();

        Double likes;
        Double dislikes;

        if(likeCount == null) {
            likes = (double) 0;
        }
        else {
            likes = likeCount.doubleValue();
        }

        if(dislikeCount == null) {
            dislikes = (double) 0;
        }
        else {
            dislikes = dislikeCount.doubleValue();
        }

        return CompletableFuture.completedFuture(likes / (likes + dislikes));
    }
}
