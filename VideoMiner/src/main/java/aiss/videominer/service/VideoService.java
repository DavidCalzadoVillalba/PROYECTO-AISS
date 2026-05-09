package aiss.videominer.service;

import aiss.videominer.model.Video;
import aiss.videominer.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository repository;
    //creamos metodo para realizar el filtro y la paginacion 
    public Page<Video> getVideos(String name, Pageable pageable) {
    if (name != null && !name.isEmpty()) {
        return repository.findByNameContaining(name, pageable);
    }
    return repository.findAll(pageable);
}

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
