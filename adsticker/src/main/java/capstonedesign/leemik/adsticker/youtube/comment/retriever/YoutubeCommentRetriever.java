package capstonedesign.leemik.adsticker.youtube.comment.retriever;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YoutubeCommentRetriever {
    private static final String API_KEY = "AIzaSyBg_339Pp9ssNABBFoc1VScZ0BIPvgR3B8";
    private final YouTube youTube;

    @Autowired
    public YoutubeCommentRetriever(YouTube youTube) throws GeneralSecurityException, IOException {
        this.youTube = youTube;
    }

    public List<String> getComments(String videoId) throws IOException {
        YouTube.CommentThreads.List request = youTube.commentThreads()
                .list("snippet")
                .setKey(API_KEY)
                .setVideoId(videoId)
                .setMaxResults(100L);

        CommentThreadListResponse commentsResponse = request.execute();
        return commentsResponse.getItems().stream()
                .map(comment -> comment.getSnippet().getTopLevelComment().getSnippet().getTextDisplay())
                .collect(Collectors.toList());
    }
}
