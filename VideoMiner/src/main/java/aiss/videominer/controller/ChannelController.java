package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import aiss.videominer.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    ChannelService service;

    @GetMapping
    public List<Channel> findAll() {
        return service.getAllChannels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Channel> findOne(@PathVariable String id) {
        Optional<Channel> channel = service.getChannelById(id);
        if (!channel.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(channel.get());
    }

    // Petición POST para guardar un canal completo
    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel) {
        Channel savedChannel = service.saveChannel(channel);
        // Devolvemos un 201 Created para seguir las buenas prácticas REST
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChannel);
    }
}