package aiss.peertubeminer.service;

import aiss.peertubeminer.model.Caption;
import aiss.peertubeminer.model.Channel;
import aiss.peertubeminer.model.Comment;
import aiss.peertubeminer.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PeerTubeService {

    @Autowired
    RestTemplate restTemplate;

    private final String BASE_URL = "https://peertube2.cpy.re/api/v1";

    // Creamos unos Records internos para mapear la respuesta JSON de PeerTube
    // que viene envuelta en un atributo "data"
    record VideoResponse(List<Video> data) {}
    record CommentResponse(List<Comment> data) {}
    record CaptionResponse(List<Caption> data) {}

    // Actualizamos la firma para recibir los límites
    public Channel getChannel(String channelId, int maxVideos, int maxComments) {
        
        // 1. Obtener los datos básicos del canal
        String channelUrl = BASE_URL + "/video-channels/" + channelId;
        System.out.println("Buscando canal en PeerTube: " + channelUrl);
        Channel channel = restTemplate.getForObject(channelUrl, Channel.class);

        if (channel == null) {
            return null; // Si no existe, cortamos aquí
        }

        // 2. Obtener los vídeos de ese canal
        String videosUrl = BASE_URL + "/video-channels/" + channelId + "/videos?count=" + maxVideos;
        System.out.println("Buscando vídeos: " + videosUrl);
        VideoResponse videoResponse = restTemplate.getForObject(videosUrl, VideoResponse.class);

        if (videoResponse != null && videoResponse.data() != null) {
            List<Video> videos = videoResponse.data();

            // 3. Por cada vídeo, buscar sus comentarios y subtítulos
            for (Video video : videos) {
                
                // Buscar comentarios
                String commentsUrl = BASE_URL + "/videos/" + video.getId() + "/comment-threads?count=" + maxComments;
                CommentResponse commentResponse = restTemplate.getForObject(commentsUrl, CommentResponse.class);
                if (commentResponse != null) {
                    video.setComments(commentResponse.data());
                }

                // Buscar subtítulos (captions)
                String captionsUrl = BASE_URL + "/videos/" + video.getId() + "/captions";
                CaptionResponse captionResponse = restTemplate.getForObject(captionsUrl, CaptionResponse.class);
                if (captionResponse != null) {
                    video.setCaptions(captionResponse.data());
                }
            }
            
            // 4. Asignamos la lista completa de vídeos (ya rellenos) a nuestro canal
            channel.setVideos(videos);
        }

        return channel;
    }
}