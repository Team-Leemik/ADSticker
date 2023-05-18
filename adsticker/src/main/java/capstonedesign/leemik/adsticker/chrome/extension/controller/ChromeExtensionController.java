package capstonedesign.leemik.adsticker.chrome.extension.controller;

import capstonedesign.leemik.adsticker.chrome.extension.data.ChromeExtensionData;
import capstonedesign.leemik.adsticker.model.controller.ModelController;
import capstonedesign.leemik.adsticker.youtube.comment.retriever.YoutubeCommentRetriever;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/url")
public class ChromeExtensionController {
    private final YoutubeCommentRetriever commentRetriever;
    private final ModelController modelController;
    private final ChromeExtensionData extensionData;
    public ChromeExtensionController(YoutubeCommentRetriever commentRetriever, ModelController modelController, ChromeExtensionData extensionData) {
        this.commentRetriever = commentRetriever;
        this.modelController = modelController;
        this.extensionData = extensionData;
    }
    @PostMapping
    public ChromeExtensionData handleUrl(@RequestBody ChromeExtensionData data) throws IOException {
        List<String> comments = commentRetriever.getComments(data.getUrl());
        float sumNLP = 0;

        for(String str : comments) {
            sumNLP += Float.parseFloat(modelController.callModel(str));
        }
        extensionData.setUrl(String.valueOf(sumNLP/comments.size()));
        // log.info(extensionData.getUrl());
        log.info(data.getUrl());
        return extensionData;
    }
}
