package aiss.peertubeminer.controller;

import aiss.peertubeminer.exception.PeertubeNotFoundException;
import aiss.peertubeminer.exception.PeertubeServiceUnavailableException;
import aiss.peertubeminer.model.Channel;
import aiss.peertubeminer.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate; // NUEVO IMPORT

@RestController
@RequestMapping("/peertube")
@Tag(name = "PeerTube Miner", description = "Servicio de extracción de datos de PeerTube")

public class PeerTubeController {

    @Autowired
    ChannelService service;
    @Autowired
    RestTemplate restTemplate;
    // informacion openApi 
    @Operation(
        summary = "Obtener datos de un canal",
        description = "Busca la información de un canal de peertube por su ID y la devuelve en formato JSON. No guarda nada en base de datos."
    )
    @ApiResponse(responseCode = "200", description = "Canal encontrado y devuelto correctamente")
    @ApiResponse(responseCode = "404", description = "El canal no existe en Peertube")
    // GET: solo en modo lectura
    @GetMapping("/{channelId}")
        public ResponseEntity<Channel> getChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments) throws PeertubeNotFoundException {

        System.out.println("Modo lectura: Buscando canal de Peertube" + channelId);
        Channel channel = service.getChannel(channelId, maxVideos, maxComments);
        return ResponseEntity.ok(channel);
    }
    @Operation(
        summary = "Extraer y enviar canal a VideoMiner",
        description = "Extrae los datos de Peertube y hace una petición POST automática al servicio VideoMiner para guardarlos en su base de datos."
    )
    @ApiResponse(responseCode = "200", description = "Canal minado y guardado en VideoMiner con éxito")
    @ApiResponse(responseCode = "404", description = "El canal no existe en Peertube")
    @ApiResponse(responseCode = "503", description = "Error de comunicación: VideoMiner está apagado o ha rechazado los datos")
    //POST:Envía a VideoMiner
    @PostMapping("/{channelId}")
        public ResponseEntity<Channel> createChannel(
            @PathVariable String channelId,
            @RequestParam(defaultValue = "10") Integer maxVideos,
            @RequestParam(defaultValue = "2") Integer maxComments)
            throws PeertubeNotFoundException, PeertubeServiceUnavailableException {

        System.out.println("Modo minado: Extrayendo canal de Peertube " + channelId + " para enviarlo a VideoMiner");
        
        // 1. Obtenemos los datos de la API externa que hemos traducido con jsonAlias
        Channel channel = service.getChannel(channelId, maxVideos, maxComments);

        // 2. ENVIAMOS LOS DATOS A VIDEOMINER
        String videoMinerUrl = "http://localhost:8080/channels";
        
        try {
            System.out.println("Enviando datos a VideoMiner en: " + videoMinerUrl);
            
            // postForObject hace la petición POST y le envía nuestro objeto channel en el body
            Channel savedChannel = restTemplate.postForObject(videoMinerUrl, channel, Channel.class);
            
            // Devolvemos el canal tal y como nos lo ha confirmado VideoMiner (probablemente con un ID nuevo de base de datos)
            return ResponseEntity.ok(savedChannel);
            
        } catch (Exception e) {
            System.out.println("Error al comunicarse con VideoMiner: " + e.getMessage());
            // Si VideoMiner está apagado o falla, devolvemos un error 400 (Bad Request) o 503 (Service Unavailable)
            throw new PeertubeServiceUnavailableException("Error al comunicarse con VideoMiner", e);
        }
    }
}