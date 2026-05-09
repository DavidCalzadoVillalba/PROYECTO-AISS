package aiss.videominer.controller;

import aiss.videominer.exception.VideoMinerException;
import aiss.videominer.model.Video;
import aiss.videominer.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    // creamos la funcion que permite obtener los videos con paginacion, filtrado y ordenacion
    @Operation(summary = "Listar vídeos con filtros", description = "Permite paginar y filtrar vídeos por nombre")
    @GetMapping
    public ResponseEntity<List<Video>> findAll(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,asc") String sort) {

    String[] sortParams = sort.split(",");
    Sort sorting = Sort.by(sortParams[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortParams[0]);
    Pageable pageable = PageRequest.of(page, size, sorting);

    Page<Video> videoPage = service.getVideos(name, pageable);
    
    return ResponseEntity.ok(videoPage.getContent());
}

    @GetMapping("/{id}")
    @Operation(summary = "Listar un video por su id", description = "Devuelve un video previamente guardado en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Video devuelto correctamente")
    public ResponseEntity<Video> findOne(@PathVariable String id) throws VideoMinerException {
        Optional<Video> video = service.getVideoById(id);

        if (video.isEmpty()) {
            throw new VideoMinerException("Video no encontrado: " + id);
        }

        return ResponseEntity.ok(video.get());
    }
}
