package aiss.videominer.controller;

import aiss.videominer.model.Video;
import aiss.videominer.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
@Tag(name = "Videos (VideoMiner)", description = "Operaciones de consulta para los videos en la base de datos")
public class VideoController {

    private final VideoService service;

    public VideoController(VideoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los videos", description = "Devuelve una lista con todos los videos guardados en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Lista devuelta correctamente")
    public List<Video> findAll() {
        return service.getAllVideos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar un video por su id", description = "Devuelve un video previamente guardado en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Video devuelto correctamente")
    public ResponseEntity<Video> findOne(@PathVariable String id) {
        Optional<Video> video = service.getVideoById(id);

        if (video.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(video.get());
    }
}
