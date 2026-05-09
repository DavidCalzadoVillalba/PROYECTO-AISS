package aiss.dailymotionminer.service;

import aiss.dailymotionminer.exception.DailymotionNotFoundException;
import aiss.dailymotionminer.model.Caption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaptionService {

    @Autowired
    RestTemplate restTemplate;

    private final String BASE_URL = "https://api.dailymotion.com";

    // Wrappers temporales para subtítulos
    record DailymotionSubtitleResponse(List<DailymotionSubtitle> list) {}
    record DailymotionSubtitle(String id, String language, String url) {}

    public List<Caption> getCaptions(String videoId) throws DailymotionNotFoundException {
        try {
            String url = BASE_URL + "/video/" + videoId + "/subtitles";
            DailymotionSubtitleResponse response = restTemplate.getForObject(url, DailymotionSubtitleResponse.class);

            List<Caption> captions = new ArrayList<>();
            if (response != null && response.list() != null) {
                for (DailymotionSubtitle ds : response.list()) {
                    Caption caption = new Caption();
                    caption.setId(ds.id());
                    caption.setLanguage(ds.language());
                    caption.setLink(ds.url());
                    captions.add(caption);
                }
            }
            return captions;

        } catch (HttpClientErrorException e) {
            System.out.println("No hay subtitulos en el video: " + videoId);
            throw new DailymotionNotFoundException("No hay subtitulos en el video: " + videoId, e);
        }
    }
}