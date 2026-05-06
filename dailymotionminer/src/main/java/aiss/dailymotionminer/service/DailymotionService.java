package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Caption;
import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.model.Comment;
import aiss.dailymotionminer.model.User;
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

    /* =====================================================================
     * WRAPPERS TEMPORALES (Moldes para engañar al JSON de Dailymotion)
     * ===================================================================== */

    // NUEVO: Wrapper para el Canal
    record DailymotionChannel(String id, String screenname, String description, Long created_time) {}

    // Wrappers para Vídeos y Subtítulos
    record DailymotionVideoResponse(List<DailymotionVideo> list) {}
    record DailymotionSubtitleResponse(List<DailymotionSubtitle> list) {}
    record DailymotionVideo(String id, String title, String description, Long created_time, DailymotionOwner owner, List<String> tags) {}
    record DailymotionOwner(String id, String screenname, String url, String avatar_720_url) {}
    record DailymotionSubtitle(String id, String language, String url) {}

    /* =====================================================================
     * LÓGICA PRINCIPAL DEL MINER
     * ===================================================================== */

    public Channel getChannel(String channelId, Integer maxVideos, Integer maxPages) {
        try {
            // 1. OBTENER EL CANAL BÁSICO CON EL WRAPPER
            String channelUrl = BASE_URL + "/user/" + channelId + "?fields=id,screenname,description,created_time";
            System.out.println("Buscando canal en Dailymotion: " + channelUrl);

            DailymotionChannel dc = restTemplate.getForObject(channelUrl, DailymotionChannel.class);
            if (dc == null) return null;

            // Creamos el canal limpio y lo rellenamos a mano a prueba de fallos
            Channel channel = new Channel();
            channel.setId(dc.id());
            // Si el nombre viene nulo, ponemos un texto por defecto 
            channel.setName(dc.screenname() != null ? dc.screenname() : "Nombre desconocido");
            channel.setDescription(dc.description());
            channel.setCreatedTime(dc.created_time() != null ? String.valueOf(dc.created_time()) : "Fecha desconocida");

            // 2. OBTENER LOS VÍDEOS DEL CANAL
            String videosUrl = BASE_URL + "/user/" + channelId + "/videos?fields=id,title,description,created_time,owner.id,owner.screenname,owner.url,owner.avatar_720_url,tags&limit=" + maxVideos;

            DailymotionVideoResponse videoResponse = restTemplate.getForObject(videosUrl, DailymotionVideoResponse.class);

            if (videoResponse != null && videoResponse.list() != null) {
                List<Video> videosLimpios = new ArrayList<>();

                for (DailymotionVideo dv : videoResponse.list()) {
                    Video v = new Video();
                    v.setId(dv.id());
                    // Salvavidas para los vídeos por si vienen nulos
                    v.setName(dv.title() != null ? dv.title() : "Video sin titulo");
                    v.setDescription(dv.description());
                    v.setReleaseTime(dv.created_time() != null ? String.valueOf(dv.created_time()) : "Fecha desconocida");

                    if (dv.owner() != null) {
                        User author = new User();
                        author.setId(String.valueOf((long) Math.abs(dv.owner().id().hashCode())));
                        author.setName(dv.owner().screenname());
                        author.setUser_link(dv.owner().url());
                        author.setPicture_link(dv.owner().avatar_720_url());
                        v.setAuthor(author);
                    }

                    List<Comment> comments = new ArrayList<>();
                    if (dv.tags() != null) {
                        for (String tag : dv.tags()) {
                            Comment c = new Comment();
                            c.setId(UUID.randomUUID().toString());
                            c.setText(tag);
                            c.setCreatedOn(v.getReleaseTime());
                            comments.add(c);
                        }
                    }
                    v.setComments(comments);

                    try {
                        String captionsUrl = BASE_URL + "/video/" + dv.id() + "/subtitles";
                        DailymotionSubtitleResponse captionResponse = restTemplate.getForObject(captionsUrl, DailymotionSubtitleResponse.class);

                        if (captionResponse != null && captionResponse.list() != null) {
                            List<Caption> captions = new ArrayList<>();
                            for (DailymotionSubtitle ds : captionResponse.list()) {
                                Caption caption = new Caption();
                                caption.setId(ds.id());
                                caption.setLanguage(ds.language());
                                caption.setLink(ds.url());
                                captions.add(caption);
                            }
                            v.setCaptions(captions);
                        }
                    } catch (HttpClientErrorException e) {
                        System.out.println("No hay subtítulos en el vídeo: " + dv.id());
                    }

                    videosLimpios.add(v);
                }

                channel.setVideos(videosLimpios);
            }

            return channel;

        } catch (HttpClientErrorException e) {
            System.err.println("Error 404 o conexión fallida con Dailymotion: " + e.getStatusCode());
            return null;
        }
    }
}