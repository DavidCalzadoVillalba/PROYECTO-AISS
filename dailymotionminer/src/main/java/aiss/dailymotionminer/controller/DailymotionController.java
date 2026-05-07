package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.service.DailymotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/dailymotion")
public class DailymotionController {

    @Autowired
    DailymotionService service;
    
    @Autowired
    RestTemplate restTemplate;
    // informacion y codigos para OpenApi
    @Operation(
        summary = "Obtener datos de un canal",
        description = "Busca la información de un canal de Dailymotion por su ID y la devuelve en formato JSON. No guarda nada en base de datos."
    )
    @ApiResponse(responseCode = "200", description = "Canal encontrado y devuelto correctamente")
    @ApiResponse(responseCode = "404", description = "El canal no existe en Dailymotion")
    // --- GET: MODO LECTURA ---
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

    @Operation(
        summary = "Extraer y enviar canal a VideoMiner",
        description = "Extrae los datos de Dailymotion y hace una petición POST automática al servicio VideoMiner para guardarlos en su base de datos."
    )
    @ApiResponse(responseCode = "200", description = "Canal minado y guardado en VideoMiner con éxito")
    @ApiResponse(responseCode = "404", description = "El canal no existe en Dailymotion")
    @ApiResponse(responseCode = "503", description = "Error de comunicación: VideoMiner está apagado o ha rechazado los datos")
    // --- POST: MODO MINERO (Envía a VideoMiner) ---
    @PostMapping("/{channelId}")
    public ResponseEntity<Channel> createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxPages) {

        System.out.println("Modo minado: Extrayendo canal de Dailymotion: " + channelId + " para enviarlo a VideoMiner");

        Channel channel = service.getChannel(channelId, maxVideos, maxPages);
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
    }}