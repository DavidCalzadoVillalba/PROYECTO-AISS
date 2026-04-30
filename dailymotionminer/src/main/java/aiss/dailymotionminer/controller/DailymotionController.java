package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.service.DailymotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dailymotion")
public class DailymotionController {

    @Autowired
    DailymotionService service;

    @PostMapping("/{channelId}")
    public Channel createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments) {

        System.out.println("Minando el canal de Dailymotion: " + channelId);

        Channel channel = service.getChannel(channelId);

        return channel;
    }
}