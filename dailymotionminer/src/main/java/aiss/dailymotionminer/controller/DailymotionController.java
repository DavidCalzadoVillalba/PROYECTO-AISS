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

    @GetMapping
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/{channelId}")
    public ResponseEntity<Channel> createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments) {

        System.out.println("Minando el canal de Dailymotion: " + channelId);

        Channel channel = service.getChannel(channelId);
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