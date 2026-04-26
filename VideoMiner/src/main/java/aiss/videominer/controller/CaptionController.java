package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.service.CaptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/captions")
public class CaptionController {

    private final CaptionService service;

    public CaptionController(CaptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Caption> findAll() {
        return service.getAllCaptions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caption> findOne(@PathVariable String id) {
        Optional<Caption> caption = service.getCaptionById(id);

        if (caption.isEmpty()) {
            return ResponseEntity.notFound().build(); // El Controller maneja la respuesta HTTP
        }

        return ResponseEntity.ok(caption.get());
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<Caption>> findByVideoId(@PathVariable String videoId) {
        Optional<List<Caption>> captions = service.getCaptionsByVideoId(videoId);

        if (captions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(captions.get());
    }
}
