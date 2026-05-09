package aiss.videominer.controller;

import aiss.videominer.exception.VideoMinerException;
import aiss.videominer.model.Caption;
import aiss.videominer.service.CaptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/captions")
@Tag(name = "Subtitulos (VideoMiner)", description = "Operaciones de consulta para los subtitulos en la base de datos")
public class CaptionController {

    private final CaptionService service;

    public CaptionController(CaptionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los subtitulos", description = "Devuelve una lista con todos los subtitulos guardados en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Lista devuelta correctamente")
    public List<Caption> findAll() {
        return service.getAllCaptions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar un subtitulo por su id", description = "Devuelve un subtitulo previamente guardado en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Subtitulo devuelto correctamente")
    public ResponseEntity<Caption> findOne(@PathVariable String id) throws VideoMinerException {
        Optional<Caption> caption = service.getCaptionById(id);

        if (caption.isEmpty()) {
            throw new VideoMinerException("Subtitulo no encontrado: " + id);
        }

        return ResponseEntity.ok(caption.get());
    }

    @GetMapping("/video/{videoId}")
    @Operation(summary = "Listar subtitulos por video", description = "Devuelve los subtitulos asociados a un video por su id.")
    @ApiResponse(responseCode = "200", description = "Subtitulos devueltos correctamente")
    public ResponseEntity<List<Caption>> findByVideoId(@PathVariable String videoId) throws VideoMinerException {
        Optional<List<Caption>> captions = service.getCaptionsByVideoId(videoId);

        if (captions.isEmpty()) {
            throw new VideoMinerException("Subtitulos no encontrados para el video: " + videoId);
        }

        return ResponseEntity.ok(captions.get());
    }
}
