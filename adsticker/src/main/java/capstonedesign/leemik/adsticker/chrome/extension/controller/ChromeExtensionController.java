package capstonedesign.leemik.adsticker.chrome.extension.controller;

import capstonedesign.leemik.adsticker.chrome.extension.data.ChromeExtensionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/url")
public class ChromeExtensionController {

    @PostMapping
    public ChromeExtensionData handleUrl(@RequestBody ChromeExtensionData data)
    {
        log.info(data.getUrl());

        return data;
    }
}
