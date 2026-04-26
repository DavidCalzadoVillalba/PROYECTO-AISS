package aiss.videominer.service;

import aiss.videominer.model.Video;
import aiss.videominer.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository repository;

    public VideoService(VideoRepository repository) {
        this.repository = repository;
    }

    public List<Video> getAllVideos() {
        return repository.findAll();
    }

    public Optional<Video> getVideoById(String id) {
        return repository.findById(id);
    }
}
