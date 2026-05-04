package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.service.DailymotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dailymotion")
public class DailymotionController {

    @Autowired
    DailymotionService service;

    // GET: read-only mode
    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxPages) {

        System.out.println("Modo lectura: Buscando canal de Dailymotion: " + channelId);

        Channel channel = service.getChannel(channelId, maxVideos, maxPages);
        if (channel == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(channel);
    }

    // POST: mining mode (send to VideoMiner)
    @PostMapping("/{channelId}")
    public ResponseEntity<Channel> createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxPages) {

        System.out.println("Modo minado: Extrayendo canal de Dailymotion: " + channelId);

        Channel channel = service.getChannel(channelId, maxVideos, maxPages);
        if (channel == null) {
            return ResponseEntity.notFound().build();
        }

        Channel createdChannel = service.createChannel(channel);
        if (createdChannel == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }
}