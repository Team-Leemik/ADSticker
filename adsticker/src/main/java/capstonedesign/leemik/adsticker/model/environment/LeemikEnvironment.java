package capstonedesign.leemik.adsticker.model.environment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LeemikEnvironment {
    @Value("${leemik.host}")
    String host;

    @Value("${leemik.nlp.port}")
    String NLPport;

    @Value("${leemik.ad.port}")
    String ADPort;
}
