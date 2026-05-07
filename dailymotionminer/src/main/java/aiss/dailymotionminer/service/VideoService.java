package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.User;
import aiss.dailymotionminer.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CommentService commentService;

    @Autowired
    CaptionService captionService;

    private final String BASE_URL = "https://api.dailymotion.com";

    // Wrappers temporales para vídeos
    record DailymotionVideoResponse(List<DailymotionVideo> list) {}
    record DailymotionVideo(String id, String title, String description, Long created_time, DailymotionOwner owner, List<String> tags) {}
    record DailymotionOwner(String id, String screenname, String url, String avatar_720_url) {}

    public List<Video> getVideos(String channelId, Integer maxVideos) {
        try {
            String url = BASE_URL + "/user/" + channelId + "/videos?fields=id,title,description,created_time,owner.id,owner.screenname,owner.url,owner.avatar_720_url,tags&limit=" + maxVideos;
            DailymotionVideoResponse response = restTemplate.getForObject(url, DailymotionVideoResponse.class);

            List<Video> videosLimpios = new ArrayList<>();

            if (response != null && response.list() != null) {
                for (DailymotionVideo dv : response.list()) {
                    Video v = new Video();
                    v.setId(dv.id());
                    v.setName(dv.title() != null ? dv.title() : "Video sin titulo");
                    v.setDescription(dv.description());
                    v.setReleaseTime(dv.created_time() != null ? String.valueOf(dv.created_time()) : "Fecha desconocida");

                    if (dv.owner() != null) {
                        User author = new User();
                        author.setId(String.valueOf((long) Math.abs(dv.owner().id().hashCode())));
                        author.setName(dv.owner().screenname());
                        author.setUser_link(dv.owner().url());
                        author.setPicture_link(dv.owner().avatar_720_url());
                        v.setUser(author);
                    }

                    // Delegamos la creación de comentarios y subtítulos a sus respectivos servicios
                    v.setComments(commentService.getCommentsFromTags(dv.tags(), v.getReleaseTime()));
                    v.setCaptions(captionService.getCaptions(dv.id()));

                    videosLimpios.add(v);
                }
            }
            return videosLimpios;

        } catch (HttpClientErrorException e) {
            System.out.println("Error al buscar vídeos del canal: " + channelId);
            return new ArrayList<>();
        }
    }
}