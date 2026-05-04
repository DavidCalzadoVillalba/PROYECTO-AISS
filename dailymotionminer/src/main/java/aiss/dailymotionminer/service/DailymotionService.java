package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.model.Comment;
import aiss.dailymotionminer.model.Video;
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
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String CHANNEL_FIELDS = "id,screenname,username,description,created_time";
    private static final String VIDEO_FIELDS = "id,title,description,created_time,owner,owner.screenname,owner.username,owner.url,owner.avatar_360_url";
    private static final String COMMENT_FIELDS = "id,message,created_time";

    private final String BASE_URL = "https://api.dailymotion.com";
    private final String VIDEOMINER_URL = "http://localhost:8080/channels";

    static class VideoListResponse {
        private List<Video> list;

        public List<Video> getList() {
            return list;
        }

        public void setList(List<Video> list) {
            this.list = list;
        }
    }

    static class CommentListResponse {
        private List<Comment> list;

        public List<Comment> getList() {
            return list;
        }

        public void setList(List<Comment> list) {
            this.list = list;
        }
    }

    public Channel getChannel(String channelId, int maxVideos, int maxPages) {
        String url = BASE_URL + "/user/" + channelId + "?fields=" + CHANNEL_FIELDS;
        System.out.println("Buscando datos en Dailymotion: " + url);
        try {
            Channel channel = restTemplate.getForObject(url, Channel.class);
            if (channel == null) {
                return null;
            }

            List<Video> videos = fetchVideos(channelId, maxVideos, maxPages);
            for (Video video : videos) {
                video.setComments(fetchComments(video.getId(), maxPages));
                if (video.getCaptions() == null) {
                    video.setCaptions(new ArrayList<>());
                }
                if (video.getAuthor() != null) {
                    video.getAuthor().setId(null);
                }
            }
            channel.setVideos(videos);
            return channel;

        } catch (HttpClientErrorException e) {
            System.err.println("Error al contactar con Dailymotion: " + e.getStatusCode());
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