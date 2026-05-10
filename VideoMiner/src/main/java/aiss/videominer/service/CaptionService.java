package aiss.videominer.service;

import aiss.videominer.model.Caption;
import aiss.videominer.repository.CaptionRepository;
import aiss.videominer.repository.VideoRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaptionService {

    private final CaptionRepository repository;
    private final VideoRepository videoRepository;

    public CaptionService(CaptionRepository repository, VideoRepository videoRepository) {
        this.repository = repository;
        this.videoRepository = videoRepository;
    }

    public List<Caption> getAllCaptions() {
        return repository.findAll();
    }
    
    public Optional<Caption> getCaptionById(@NonNull String id) {
        return repository.findById(id);
    }

    public Optional<List<Caption>> getCaptionsByVideoId(@NonNull String videoId) {
        if (!videoRepository.existsById(videoId)) {
            return Optional.empty();
        }

        return Optional.of(repository.findByVideoId(videoId));
    }
}
