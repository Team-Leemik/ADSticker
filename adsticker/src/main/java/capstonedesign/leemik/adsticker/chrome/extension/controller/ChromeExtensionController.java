package capstonedesign.leemik.adsticker.chrome.extension.controller;

import capstonedesign.leemik.adsticker.chrome.extension.data.ChromeExtensionData;
import capstonedesign.leemik.adsticker.model.controller.ModelController;
import capstonedesign.leemik.adsticker.youtube.comment.retriever.YoutubeCommentRetriever;
import capstonedesign.leemik.adsticker.youtube.likes.and.dislikes.retriever.YoutubeLikesAndDislikesRetriever;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequestMapping("/url")
public class ChromeExtensionController {
    private final ModelController modelController;
    private final ChromeExtensionData extensionData;
    private final YoutubeLikesAndDislikesRetriever likesAndDislikesRetriever;
    @Autowired
     ChromeExtensionController(YoutubeLikesAndDislikesRetriever likesAndDislikesRetriever, ModelController modelController, ChromeExtensionData extensionData) {
        this.modelController = modelController;
        this.extensionData = extensionData;
        this.likesAndDislikesRetriever = likesAndDislikesRetriever;
    }
    @PostMapping
    public ChromeExtensionData handleUrl(@RequestBody ChromeExtensionData data) throws IOException, ExecutionException, InterruptedException {
        log.info(data.toString());
        CompletableFuture<Double> NLPFuture = modelController.callModel(data.getUrl());
        CompletableFuture<Double> likesAndDislikesFuture = likesAndDislikesRetriever.getLikesAndDislikes(data.getUrl());

        extensionData.setUrl(String.valueOf(0.7* NLPFuture.get() + 0.3*likesAndDislikesFuture.get()));
        log.info(data.getUrl());
        return extensionData;
    }
}
