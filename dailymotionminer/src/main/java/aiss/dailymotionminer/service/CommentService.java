package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    public List<Comment> getCommentsFromTags(List<String> tags, String releaseTime) {
        List<Comment> comments = new ArrayList<>();
        
        if (tags != null) {
            for (String tag : tags) {
                Comment c = new Comment();
                c.setId(UUID.randomUUID().toString());
                c.setText(tag);
                c.setCreatedOn(releaseTime);
                comments.add(c);
            }
        }
        return comments;
    }
}