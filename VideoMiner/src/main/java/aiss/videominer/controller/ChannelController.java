package aiss.videominer.controller;

import aiss.videominer.exception.VideoMinerException;
import aiss.videominer.model.Channel;
import aiss.videominer.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/channels")
@Tag(name = "Canales (VideoMiner)", description = "Operaciones CRUD para los canales en la base de datos")
public class ChannelController {

    @Autowired
    ChannelService service;
    
    @Operation(summary = "Listar todos los canales", description = "Devuelve una lista con todos los canales guardados en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Lista devuelta correctamente")
    @GetMapping
    public List<Channel> findAll() {
        return service.getAllChannels();
    }
    @Operation(summary = "Listar un canal por su id (nombre de usuario)", description = "Devuelve un canal previamente guardado en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Canal devuelto correctamente")
    @GetMapping("/{id}")
    public ResponseEntity<Channel> findOne(@PathVariable @NonNull String id) throws VideoMinerException {
        Optional<Channel> channel = service.getChannelById(id);
        if (!channel.isPresent()) {
            throw new VideoMinerException("Canal no encontrado: " + id);
        }
        return ResponseEntity.ok(channel.get());
    }

    @PostMapping
    @Operation(summary = "Crear un canal", description = "Guarda un canal en la base de datos H2 y devuelve el recurso creado.")
    @ApiResponse(responseCode = "201", description = "Canal creado correctamente")
    public ResponseEntity<Channel> createChannel(@RequestBody @NonNull Channel channel) {
        Channel savedChannel = service.saveChannel(channel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChannel);
    }
}