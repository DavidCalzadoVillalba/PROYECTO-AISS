package aiss.peertubeminer.service;

import aiss.peertubeminer.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    private final String BASE_URL = "https://video.blender.org/api/v1";
    
    record CommentResponse(List<Comment> data) {}

    public List<Comment> getComments(String videoId, int maxComments) {
        try {
            String url = BASE_URL + "/videos/" + videoId + "/comment-threads?count=" + maxComments;
            CommentResponse response = restTemplate.getForObject(url, CommentResponse.class);
            return (response != null && response.data() != null) ? response.data() : new ArrayList<>();
        } catch (HttpClientErrorException e) {
            System.out.println("No hay comentarios (o falló) en el vídeo: " + videoId);
            return new ArrayList<>();
        }
    }
}