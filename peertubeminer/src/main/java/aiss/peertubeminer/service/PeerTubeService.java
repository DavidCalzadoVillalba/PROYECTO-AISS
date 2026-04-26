package aiss.peertubeminer.service;

import aiss.peertubeminer.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PeerTubeService {

    @Autowired
    RestTemplate restTemplate;

    private final String BASE_URL = "https://peertube2.cpy.re/api/v1";

    public Channel getChannel(String channelId) {
        String url = BASE_URL + "/video-channels/" + channelId;
        System.out.println("Buscando datos en PeerTube: " + url);
        Channel channel = restTemplate.getForObject(url, Channel.class);

        return channel;
    }
}