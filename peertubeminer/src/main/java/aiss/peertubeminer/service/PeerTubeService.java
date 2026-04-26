package aiss.peertubeminer.service;

import aiss.peertubeminer.model.Caption;
import aiss.peertubeminer.model.Channel;
import aiss.peertubeminer.model.Comment;
import aiss.peertubeminer.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException; // IMPORTANTE
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PeerTubeService {

    @Autowired
    RestTemplate restTemplate;

    // Cambiamos a una instancia que sabemos que tiene muchos datos reales
    private final String BASE_URL = "https://video.blender.org/api/v1";

    record VideoResponse(List<Video> data) {}
    record CommentResponse(List<Comment> data) {}
    record CaptionResponse(List<Caption> data) {}

    public Channel getChannel(String channelId, int maxVideos, int maxComments) {
        try {
            // 1. Obtener los datos básicos del canal
            String channelUrl = BASE_URL + "/video-channels/" + channelId;
            System.out.println("Buscando canal en PeerTube: " + channelUrl);
            Channel channel = restTemplate.getForObject(channelUrl, Channel.class);

            if (channel == null) return null;

            // 2. Obtener los vídeos
            String videosUrl = BASE_URL + "/video-channels/" + channelId + "/videos?count=" + maxVideos;
            VideoResponse videoResponse = restTemplate.getForObject(videosUrl, VideoResponse.class);

            if (videoResponse != null && videoResponse.data() != null) {
                List<Video> videos = videoResponse.data();

                // 3. Por cada vídeo, buscar sus comentarios y subtítulos
                for (Video video : videos) {
                    try {
                        String commentsUrl = BASE_URL + "/videos/" + video.getId() + "/comment-threads?count=" + maxComments;
                        CommentResponse commentResponse = restTemplate.getForObject(commentsUrl, CommentResponse.class);
                        if (commentResponse != null) video.setComments(commentResponse.data());
                    } catch (HttpClientErrorException e) {
                        System.out.println("No hay comentarios (o falló) en el vídeo: " + video.getId());
                    }

                    try {
                        String captionsUrl = BASE_URL + "/videos/" + video.getId() + "/captions";
                        CaptionResponse captionResponse = restTemplate.getForObject(captionsUrl, CaptionResponse.class);
                        if (captionResponse != null) video.setCaptions(captionResponse.data());
                    } catch (HttpClientErrorException e) {
                        System.out.println("No hay subtítulos (o falló) en el vídeo: " + video.getId());
                    }
                }
                channel.setVideos(videos);
            }
            return channel;

        } catch (HttpClientErrorException e) {
            // AQUÍ ATRAPAMOS EL ERROR. Si el canal no existe, ya no crashea, devuelve null.
            System.out.println("Error en PeerTube al buscar el canal. Código: " + e.getStatusCode());
            return null;
        }
    }
}