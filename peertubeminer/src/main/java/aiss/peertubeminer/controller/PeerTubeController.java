package aiss.peertubeminer.controller;

import aiss.peertubeminer.model.Channel;
import aiss.peertubeminer.service.PeerTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/peertube")
public class PeerTubeController {

    @Autowired
    PeerTubeService service;

    @PostMapping("/{channelId}")
    public Channel createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments) {

        System.out.println("Minando canal: " + channelId);
        Channel channel = service.getChannel(channelId);

        return channel;
    }
}