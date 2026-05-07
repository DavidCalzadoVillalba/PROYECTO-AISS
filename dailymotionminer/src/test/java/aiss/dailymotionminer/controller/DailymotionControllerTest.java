package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.service.DailymotionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DailymotionController.class)
class DailymotionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private DailymotionService service;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void testGetChannelExists() throws Exception {
        System.out.println("EJECUTANDO TEST: Comprobando GET de un canal de Dailymotion existente...");

        // Creamos un canal falso para no depender de la API real de Dailymotion
        Channel channel = new Channel();
        channel.setId("news");
        channel.setName("Canal de noticias");

        // Simulamos que el service encuentra el canal
        when(service.getChannel("news", 2, 1)).thenReturn(channel);

        // Hacemos una petición GET falsa al controller
        mockMvc.perform(get("/dailymotion/news")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("news"))
                .andExpect(jsonPath("$.name").value("Canal de noticias"));
    }

    @Test
    void testGetChannelNotExists() throws Exception {
        System.out.println("EJECUTANDO TEST: Comprobando GET de un canal de Dailymotion que no existe...");

        // Simulamos que el service no encuentra el canal
        when(service.getChannel("canal_falso", 2, 1)).thenReturn(null);

        // El controller debería devolver 404
        mockMvc.perform(get("/dailymotion/canal_falso")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostChannelExists() throws Exception {
        System.out.println("EJECUTANDO TEST: Comprobando POST de Dailymotion hacia VideoMiner...");

        // Creamos un canal falso
        Channel channel = new Channel();
        channel.setId("news");
        channel.setName("Canal de noticias");

        // Simulamos que DailymotionService encuentra el canal
        when(service.getChannel("news", 2, 1)).thenReturn(channel);

        // Simulamos que VideoMiner guarda el canal correctamente
        when(restTemplate.postForObject(
                ArgumentMatchers.eq("http://localhost:8080/channels"),
                ArgumentMatchers.any(Channel.class),
                ArgumentMatchers.eq(Channel.class)
        )).thenReturn(channel);

        // Hacemos una petición POST falsa al controller
        mockMvc.perform(post("/dailymotion/news")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("news"))
                .andExpect(jsonPath("$.name").value("Canal de noticias"));
    }

    @Test
    void testPostChannelNotExists() throws Exception {
        System.out.println("EJECUTANDO TEST: Comprobando POST de un canal de Dailymotion que no existe...");

        // Simulamos que el canal no existe
        when(service.getChannel("canal_falso", 2, 1)).thenReturn(null);

        // El controller debería devolver 404
        mockMvc.perform(post("/dailymotion/canal_falso")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostVideoMinerError() throws Exception {
        System.out.println("EJECUTANDO TEST: Comprobando error cuando VideoMiner no responde...");

        // Creamos un canal falso
        Channel channel = new Channel();
        channel.setId("news");
        channel.setName("Canal de noticias");

        // Simulamos que DailymotionService encuentra el canal
        when(service.getChannel("news", 2, 1)).thenReturn(channel);

        // Simulamos que VideoMiner está apagado o falla
        when(restTemplate.postForObject(
                ArgumentMatchers.eq("http://localhost:8080/channels"),
                ArgumentMatchers.any(Channel.class),
                ArgumentMatchers.eq(Channel.class)
        )).thenThrow(new RuntimeException("VideoMiner no disponible"));

        // El controller debería devolver 503
        mockMvc.perform(post("/dailymotion/news")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isServiceUnavailable());
    }
}