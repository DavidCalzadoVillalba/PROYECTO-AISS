package aiss.peertubeminer.service;

import aiss.peertubeminer.exception.PeertubeNotFoundException;
import aiss.peertubeminer.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelService {

    @Autowired
    RestTemplate restTemplate;

    // Inyectamos el servicio de vídeos
    @Autowired
    VideoService videoService;

    private final String BASE_URL = "https://video.blender.org/api/v1";

    public Channel getChannel(String channelId, int maxVideos, int maxComments) throws PeertubeNotFoundException {
        try {
            String url = BASE_URL + "/video-channels/" + channelId;
            System.out.println("Buscando canal en PeerTube: " + url);
            Channel channel = restTemplate.getForObject(url, Channel.class);

            if (channel == null) {
                throw new PeertubeNotFoundException("Canal no encontrado: " + channelId);
            }

            // enviamos la búsqueda de vídeos y sus detalles al VideoService
            channel.setVideos(videoService.getVideos(channelId, maxVideos, maxComments));

            return channel;

        } catch (HttpClientErrorException e) {
            System.out.println("Error en PeerTube al buscar el canal. Codigo: " + e.getStatusCode());
            throw new PeertubeNotFoundException("Error al obtener el canal: " + channelId, e);
        }
    }
}