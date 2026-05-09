package aiss.peertubeminer.service;

import aiss.peertubeminer.exception.PeertubeNotFoundException;
import aiss.peertubeminer.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    RestTemplate restTemplate;

    // Inyectamos los otros servicios para delegarles el trabajo
    @Autowired
    CommentService commentService;

    @Autowired
    CaptionService captionService;

    private final String BASE_URL = "https://video.blender.org/api/v1";
    
    record VideoResponse(List<Video> data) {}

    public List<Video> getVideos(String channelId, int maxVideos, int maxComments) throws PeertubeNotFoundException {
        try {
            String url = BASE_URL + "/video-channels/" + channelId + "/videos?count=" + maxVideos;
            System.out.println("Buscando vídeos en PeerTube: " + url);
            VideoResponse response = restTemplate.getForObject(url, VideoResponse.class);

            List<Video> videos = (response != null && response.data() != null) ? response.data() : new ArrayList<>();

            // Por cada vídeo, llamamos a los otros servicios para rellenarlo
            for (Video video : videos) {
                video.setComments(commentService.getComments(video.getId(), maxComments));
                video.setCaptions(captionService.getCaptions(video.getId()));
            }

            return videos;

        } catch (HttpClientErrorException e) {
            System.out.println("Error al buscar videos del canal: " + channelId);
            throw new PeertubeNotFoundException("Error al buscar videos del canal: " + channelId, e);
        }
    }
}