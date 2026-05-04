package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Caption;
import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.model.Comment;
import aiss.dailymotionminer.model.Video;
import aiss.dailymotionminer.service.DailymotionService.VideoListResponse.CommentListResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DailymotionService {

    @Autowired
    RestTemplate restTemplate;
    private final String BASE_URL = "https://api.dailymotion.com"; //TODO

    record VideoResponse(List<Video> list) {}
    record CommentResponse(List<Comment> list) {}
    record SubtitleResponse(List<Caption> list) {}

    public Channel getChannel(String userId, int maxVideos) {
        try {
            // 1. Obtener datos del canal (usuario)
            // Usamos fields para asegurar que traemos el nombre y descripción
            String channelUrl = BASE_URL + "/user/" + userId ;
            
            Channel channel = restTemplate.getForObject(channelUrl, Channel.class);

            if (channel == null) return null;

            // 2. Obtener los vídeos del usuario
            // Dailymotion usa 'limit' en vez de 'count'
            String videosUrl = BASE_URL + "/user/" + userId + "/videos?limit=" + maxVideos + "&fields=id,title,description";
            VideoResponse videoResponse = restTemplate.getForObject(videosUrl, VideoResponse.class);

            if (videoResponse != null && videoResponse.list() != null) {
                List<Video> videos = videoResponse.list();

                // 3. Por cada vídeo, buscar sus comentarios
                for (Video video : videos) {
                    try {
                        String commentsUrl = BASE_URL + "/video/" + video.getId() + "/comments?fields=message,created_time";
                        CommentResponse commentResponse = restTemplate.getForObject(commentsUrl, CommentResponse.class);
                        
                        if (commentResponse != null) {
                            video.setComments(commentResponse.list());
                        }
                    } catch (HttpClientErrorException e) {
                        System.out.println("Error obteniendo comentarios para: " + video.getId());
                    }
                }
                channel.setVideos(videos);
            }
            return channel;

        } catch (HttpClientErrorException e) {
            System.out.println("Error en Dailymotion. Código: " + e.getStatusCode());
            return null;
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

    private List<Video> fetchVideos(String channelId, int maxVideos, int maxPages) {
        List<Video> videos = new ArrayList<>();
        int page = 1;
        int remaining = Math.max(0, maxVideos);

        while (page <= maxPages && remaining > 0) {
            int pageSize = Math.min(DEFAULT_PAGE_SIZE, remaining);
            String videosUrl = BASE_URL + "/user/" + channelId + "/videos" +
                    "?limit=" + pageSize + "&page=" + page + "&fields=" + VIDEO_FIELDS;

            VideoListResponse response = restTemplate.getForObject(videosUrl, VideoListResponse.class);
            if (response == null || response.getList() == null || response.getList().isEmpty()) {
                break;
            }

            videos.addAll(response.getList());
            remaining = maxVideos - videos.size();
            if (response.getList().size() < pageSize) {
                break;
            }
            page++;
        }

        return videos;
    }

    private List<Comment> fetchComments(String videoId, int maxPages) {
        List<Comment> comments = new ArrayList<>();
        int page = 1;

        while (page <= maxPages) {
            String commentsUrl = BASE_URL + "/video/" + videoId + "/comments" +
                    "?limit=" + DEFAULT_PAGE_SIZE + "&page=" + page + "&fields=" + COMMENT_FIELDS;

            CommentListResponse response = restTemplate.getForObject(commentsUrl, CommentListResponse.class);
            if (response == null || response.getList() == null || response.getList().isEmpty()) {
                break;
            }

            comments.addAll(response.getList());
            if (response.getList().size() < DEFAULT_PAGE_SIZE) {
                break;
            }
            page++;
        }

        return comments;
    }
}