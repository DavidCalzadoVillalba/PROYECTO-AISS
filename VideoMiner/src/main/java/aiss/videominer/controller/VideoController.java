package aiss.videominer.controller;

import aiss.videominer.model.Video;
import aiss.videominer.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService service;

    public VideoController(VideoService service) {
        this.service = service;
    }

    // Operación para listar todos los vídeos
    @GetMapping
    public List<Video> findAll() {
        return service.getAllVideos();
    }

    // Operación para buscar un vídeo por id
    @GetMapping("/{id}")
    public ResponseEntity<Video> findOne(@PathVariable String id) {
        Optional<Video> video = service.getVideoById(id);

        if (video.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(video.get());
    }
}
