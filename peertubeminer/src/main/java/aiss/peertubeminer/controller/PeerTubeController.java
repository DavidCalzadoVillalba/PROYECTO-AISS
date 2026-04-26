package aiss.peertubeminer.controller;

import aiss.peertubeminer.model.Channel;
import aiss.peertubeminer.service.PeerTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/peertube")
public class PeerTubeController {

    @Autowired
    PeerTubeService service;

    // --- NUEVO: MÉTODO GET PARA PRUEBAS (Solo lee) ---
    @GetMapping()
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments) {

        System.out.println("Modo lectura: Buscando canal " + channelId);
        Channel channel = service.getChannel(channelId, maxVideos, maxComments);

        if (channel == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(channel);
    }

    // --- EL POST ORIGINAL (Aún pendiente de enviar a VideoMiner) ---
    @PostMapping("/{channelId}")
    public ResponseEntity<Channel> createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments) {

        System.out.println("Modo minado: Extrayendo canal " + channelId + " para enviarlo a VideoMiner");
        Channel channel = service.getChannel(channelId, maxVideos, maxComments);

        if (channel == null) {
            return ResponseEntity.notFound().build();
        }

        // TODO: Aquí nos falta el código para enviar 'channel' a VideoMiner
        
        return ResponseEntity.ok(channel);
    }
}