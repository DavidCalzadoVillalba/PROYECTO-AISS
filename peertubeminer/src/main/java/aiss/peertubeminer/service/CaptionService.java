package aiss.peertubeminer.service;

import aiss.peertubeminer.exception.PeertubeNotFoundException;
import aiss.peertubeminer.model.Caption;
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

    private final String BASE_URL = "https://video.blender.org/api/v1";
    
    record CaptionResponse(List<Caption> data) {}

    public List<Caption> getCaptions(String videoId) throws PeertubeNotFoundException {
        try {
            String url = BASE_URL + "/videos/" + videoId + "/captions";
            CaptionResponse response = restTemplate.getForObject(url, CaptionResponse.class);
            return (response != null && response.data() != null) ? response.data() : new ArrayList<>();
        } catch (HttpClientErrorException e) {
            System.out.println("No hay subtitulos (o fallo) en el video: " + videoId);
            throw new PeertubeNotFoundException("No hay subtitulos en el video: " + videoId, e);
        }
    }
}