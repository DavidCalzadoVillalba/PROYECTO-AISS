package aiss.videominer.service;

import aiss.videominer.model.Comment;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository repository;
    private final VideoRepository videoRepository;

    public CommentService(CommentRepository repository, VideoRepository videoRepository) {
        this.repository = repository;
        this.videoRepository = videoRepository;
    }

    public List<Comment> getAllComments() {
        return repository.findAll();
    }

    public Optional<Comment> getCommentById(String id) {
        return repository.findById(id);
    }

    public Optional<List<Comment>> getCommentsByVideoId(String videoId) {
        if (!videoRepository.existsById(videoId)) {
            return Optional.empty();
        }

        return Optional.of(repository.findByVideoId(videoId));
    }
}
