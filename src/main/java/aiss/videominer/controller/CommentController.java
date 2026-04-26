package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Comment> findAll() {
        return service.getAllComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findOne(@PathVariable String id) {
        Optional<Comment> comment = service.getCommentById(id);

        if (comment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comment.get());
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<Comment>> findByVideoId(@PathVariable String videoId) {
        Optional<List<Comment>> comments = service.getCommentsByVideoId(videoId);

        if (comments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comments.get());
    }
}
