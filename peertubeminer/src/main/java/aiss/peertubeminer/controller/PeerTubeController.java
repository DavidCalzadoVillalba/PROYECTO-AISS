package aiss.peertubeminer.controller;

import aiss.peertubeminer.model.Channel;
import aiss.peertubeminer.service.PeerTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate; // NUEVO IMPORT

@RestController
@RequestMapping("/peertube")
public class PeerTubeController {

    @Autowired
    PeerTubeService service;

    // Inyectamos RestTemplate para poder enviar datos a VideoMiner
    @Autowired
    RestTemplate restTemplate;

    // --- GET: MODO LECTURA ---
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

    // --- POST: MODO MINERO (Envía a VideoMiner) ---
    @PostMapping("/{channelId}")
    public ResponseEntity<Channel> createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments) {

        System.out.println("Modo minado: Extrayendo canal " + channelId + " para enviarlo a VideoMiner");
        
        // 1. Obtenemos los datos de la API externa (ya traducidos gracias a @JsonAlias)
        Channel channel = service.getChannel(channelId, maxVideos, maxComments);

        if (channel == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. ENVIAMOS LOS DATOS A VIDEOMINER
        // Asumimos que VideoMiner corre en el puerto 8080 y su endpoint es /channels
        String videoMinerUrl = "http://localhost:8080/channels";
        
        try {
            System.out.println("Enviando datos a VideoMiner en: " + videoMinerUrl);
            
            // postForObject hace la petición POST y le envía nuestro objeto 'channel' en el body
            Channel savedChannel = restTemplate.postForObject(videoMinerUrl, channel, Channel.class);
            
            // Devolvemos el canal tal y como nos lo ha confirmado VideoMiner (probablemente con un ID nuevo de base de datos)
            return ResponseEntity.ok(savedChannel);
            
        } catch (Exception e) {
            System.out.println("Error al comunicarse con VideoMiner: " + e.getMessage());
            // Si VideoMiner está apagado o falla, devolvemos un error 400 (Bad Request) o 503 (Service Unavailable)
            return ResponseEntity.status(503).build(); 
        }
    }
}