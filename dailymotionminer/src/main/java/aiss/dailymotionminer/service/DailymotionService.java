package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class DailymotionService {

    @Autowired
    RestTemplate restTemplate;
    private final String BASE_URL = "https://api.dailymotion.com";
    private final String VIDEOMINER_URL = "http://localhost:8080/channels";

    public Channel getChannel(String channelId) {
        String url = BASE_URL + "/user/" + channelId;
        System.out.println("Buscando datos en Dailymotion: " + url);
        try {
            Channel channel = restTemplate.getForObject(url, Channel.class);
            return channel;

        } catch (HttpClientErrorException e) {
            System.err.println("Error al contactar con Dailymotion: " + e.getStatusCode());
            throw e;
        }
    }

    public Channel createChannel(Channel channel) {
        try {
            return restTemplate.postForObject(VIDEOMINER_URL, channel, Channel.class);
        } catch (HttpClientErrorException e) {
            System.err.println("Error al enviar el canal a VideoMiner: " + e.getStatusCode());
            return null;
        }
    }
}