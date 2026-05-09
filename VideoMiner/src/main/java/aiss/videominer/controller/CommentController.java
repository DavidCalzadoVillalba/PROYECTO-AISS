package aiss.videominer.controller;

import aiss.videominer.exception.VideoMinerException;
import aiss.videominer.model.Comment;
import aiss.videominer.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@Tag(name = "Comentarios (VideoMiner)", description = "Operaciones de consulta para los comentarios en la base de datos")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los comentarios", description = "Devuelve una lista con todos los comentarios guardados en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Lista devuelta correctamente")
    public List<Comment> findAll() {
        return service.getAllComments();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar un comentario por su id", description = "Devuelve un comentario previamente guardado en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Comentario devuelto correctamente")
    public ResponseEntity<Comment> findOne(@PathVariable String id) throws VideoMinerException {
        Optional<Comment> comment = service.getCommentById(id);

        if (comment.isEmpty()) {
            throw new VideoMinerException("Comentario no encontrado: " + id);
        }

        return ResponseEntity.ok(comment.get());
    }

    @GetMapping("/video/{videoId}")
    @Operation(summary = "Listar comentarios por video", description = "Devuelve los comentarios asociados a un video por su id.")
    @ApiResponse(responseCode = "200", description = "Comentarios devueltos correctamente")
    public ResponseEntity<List<Comment>> findByVideoId(@PathVariable String videoId) throws VideoMinerException {
        Optional<List<Comment>> comments = service.getCommentsByVideoId(videoId);

        if (comments.isEmpty()) {
            throw new VideoMinerException("Comentarios no encontrados para el video: " + videoId);
        }

        return ResponseEntity.ok(comments.get());
    }
}
