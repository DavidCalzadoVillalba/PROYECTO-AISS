package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Caption;
import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.model.Comment;
import aiss.dailymotionminer.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DailymotionService {

    @Autowired
    RestTemplate restTemplate;

    private final String BASE_URL = "https://api.dailymotion.com";

    // Mismos records que en PeerTube, adaptados a la etiqueta "list" de Dailymotion
    record VideoResponse(List<Video> list) {}
    record SubtitleResponse(List<Caption> list) {}

    public Channel getChannel(String channelId, int maxVideos, int maxPages) {
        try {
            // 1. Obtener los datos básicos del canal
            String channelUrl = BASE_URL + "/user/" + channelId + "?fields=id,screenname,description,created_time";
            System.out.println("Buscando canal en Dailymotion: " + channelUrl);
            Channel channel = restTemplate.getForObject(channelUrl, Channel.class);

            if (channel == null) return null;

            List<Video> allVideos = new ArrayList<>();

            // 2. Obtener los vídeos (Añadimos un bucle para cumplir con maxPages)
            for (int i = 1; i <= maxPages; i++) {
                String videosUrl = BASE_URL + "/user/" + channelId + "/videos?limit=" + maxVideos + "&page=" + i + "&fields=id,title,description,created_time,tags,owner.id,owner.screenname,owner.avatar_360_url";
                System.out.println("Buscando vídeos en Dailymotion (página " + i + "): " + videosUrl);
                VideoResponse videoResponse = restTemplate.getForObject(videosUrl, VideoResponse.class);

                if (videoResponse != null && videoResponse.list() != null && !videoResponse.list().isEmpty()) {
                    List<Video> videos = videoResponse.list();

                    // 3. Por cada vídeo, buscar sus comentarios y subtítulos
                    for (Video video : videos) {
                        
                        // 3.1. Dailymotion NO tiene comentarios. Convertimos los "tags" a Comentarios según el PDF
                        List<Comment> fakeComments = new ArrayList<>();
                        if (video.getTags() != null) {
                            for (String tag : video.getTags()) {
                                Comment comment = new Comment();
                                comment.setId(UUID.randomUUID().toString()); // Inventamos un ID aleatorio
                                comment.setText(tag);
                                comment.setCreatedOn(video.getReleaseTime());
                                fakeComments.add(comment);
                            }
                        }
                        video.setComments(fakeComments);

                        // 3.2. Buscar subtítulos (captions) manteniendo tu mismo try-catch de PeerTube
                        try {
                            String subtitlesUrl = BASE_URL + "/video/" + video.getId() + "/subtitles";
                            SubtitleResponse subtitleResponse = restTemplate.getForObject(subtitlesUrl, SubtitleResponse.class);
                            if (subtitleResponse != null) video.setCaptions(subtitleResponse.list());
                        } catch (HttpClientErrorException e) {
                            System.out.println("No hay subtítulos (o falló) en el vídeo: " + video.getId());
                        }
                    }
                    
                    allVideos.addAll(videos);
                } else {
                    break; // Si la página viene vacía, rompemos el bucle
                }
            }
            
            channel.setVideos(allVideos);
            return channel;

        } catch (HttpClientErrorException e) {
            // Aqui pillamos el error que salte, si el canal no existe o hay algun otro problema y devolvemos null + mensaje de error
            System.out.println("Error en Dailymotion al buscar el canal. Código: " + e.getStatusCode());
            return null;
        }
    }
}