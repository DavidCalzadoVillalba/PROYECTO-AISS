package aiss.dailymotionminer.controller;

import aiss.dailymotionminer.model.Channel;
import aiss.dailymotionminer.service.DailymotionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DailymotionController.class)
class DailymotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailymotionService service;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void getChannel_WhenChannelExists_ShouldReturnChannel() throws Exception {
        Channel channel = new Channel();
        channel.setId("news");
        channel.setName("Canal de noticias");

        when(service.getChannel("news", 2, 1)).thenReturn(channel);

        mockMvc.perform(get("/dailymotion/news")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("news"))
                .andExpect(jsonPath("$.name").value("Canal de noticias"));

        verify(service).getChannel("news", 2, 1);
    }

    @Test
    void getChannel_WhenChannelDoesNotExist_ShouldReturn404() throws Exception {
        when(service.getChannel("canal_falso", 2, 1)).thenReturn(null);

        mockMvc.perform(get("/dailymotion/canal_falso")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isNotFound());

        verify(service).getChannel("canal_falso", 2, 1);
    }

    @Test
    void postChannel_WhenChannelExistsAndVideoMinerWorks_ShouldReturnOk() throws Exception {
        Channel channel = new Channel();
        channel.setId("news");
        channel.setName("Canal de noticias");

        when(service.getChannel("news", 2, 1)).thenReturn(channel);

        when(restTemplate.postForObject(
                ArgumentMatchers.eq("http://localhost:8080/channels"),
                ArgumentMatchers.any(Channel.class),
                ArgumentMatchers.eq(Channel.class)
        )).thenReturn(channel);

        mockMvc.perform(post("/dailymotion/news")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("news"))
                .andExpect(jsonPath("$.name").value("Canal de noticias"));

        verify(service).getChannel("news", 2, 1);

        verify(restTemplate).postForObject(
                ArgumentMatchers.eq("http://localhost:8080/channels"),
                ArgumentMatchers.any(Channel.class),
                ArgumentMatchers.eq(Channel.class)
        );
    }

    @Test
    void postChannel_WhenChannelDoesNotExist_ShouldReturn404() throws Exception {
        when(service.getChannel("canal_falso", 2, 1)).thenReturn(null);

        mockMvc.perform(post("/dailymotion/canal_falso")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isNotFound());

        verify(service).getChannel("canal_falso", 2, 1);
    }

    @Test
    void postChannel_WhenVideoMinerFails_ShouldReturn503() throws Exception {
        Channel channel = new Channel();
        channel.setId("news");
        channel.setName("Canal de noticias");

        when(service.getChannel("news", 2, 1)).thenReturn(channel);

        when(restTemplate.postForObject(
                ArgumentMatchers.eq("http://localhost:8080/channels"),
                ArgumentMatchers.any(Channel.class),
                ArgumentMatchers.eq(Channel.class)
        )).thenThrow(new RuntimeException("VideoMiner apagado"));

        mockMvc.perform(post("/dailymotion/news")
                        .param("maxVideos", "2")
                        .param("maxPages", "1"))
                .andExpect(status().isServiceUnavailable());

        verify(service).getChannel("news", 2, 1);
    }
}