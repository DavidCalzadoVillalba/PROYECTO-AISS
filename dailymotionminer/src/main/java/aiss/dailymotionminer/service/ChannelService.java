package aiss.dailymotionminer.service;

import aiss.dailymotionminer.exception.DailymotionNotFoundException;
import aiss.dailymotionminer.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    VideoService videoService;

    private final String BASE_URL = "https://api.dailymotion.com";

    // Wrapper para el Canal
    record DailymotionChannel(String id, String screenname, String description, Long created_time) {}

    public Channel getChannel(String channelId, Integer maxVideos, Integer maxPages) throws DailymotionNotFoundException {
        try {
            String url = BASE_URL + "/user/" + channelId + "?fields=id,screenname,description,created_time";
            System.out.println("Buscando canal en Dailymotion: " + url);

            DailymotionChannel dc = restTemplate.getForObject(url, DailymotionChannel.class);
            if (dc == null) {
                throw new DailymotionNotFoundException("Canal no encontrado: " + channelId);
            }

            Channel channel = new Channel();
            channel.setId(dc.id());
            channel.setName(dc.screenname() != null ? dc.screenname() : "Nombre desconocido");
            channel.setDescription(dc.description());
            channel.setCreatedTime(dc.created_time() != null ? String.valueOf(dc.created_time()) : "Fecha desconocida");

            // Delegamos la búsqueda de vídeos al VideoService
            channel.setVideos(videoService.getVideos(channelId, maxVideos));

            return channel;

        } catch (HttpClientErrorException e) {
            System.err.println("Error 404 o conexion fallida con Dailymotion: " + e.getStatusCode());
            throw new DailymotionNotFoundException("Error al obtener el canal: " + channelId, e);
        }
    }
}