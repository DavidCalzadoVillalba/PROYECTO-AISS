package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.service.CaptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CaptionController.class)
class CaptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaptionService service;

    @Test
    void testGetAllCaptions() throws Exception {
        System.out.println("Test para comprobar que se devuelven todas las captions");

        Caption caption1 = new Caption();
        caption1.setId("caption1");
        caption1.setLanguage("es");
        caption1.setLink("https://example.com/caption1");

        Caption caption2 = new Caption();
        caption2.setId("caption2");
        caption2.setLanguage("en");
        caption2.setLink("https://example.com/caption2");

        when(service.getAllCaptions()).thenReturn(Arrays.asList(caption1, caption2));

        mockMvc.perform(get("/captions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("caption1"))
                .andExpect(jsonPath("$[0].language").value("es"))
                .andExpect(jsonPath("$[1].id").value("caption2"))
                .andExpect(jsonPath("$[1].language").value("en"));
    }

    @Test
    void testGetCaptionById() throws Exception {
        System.out.println("Test para comprobar que se devuelve una caption por su id");

        Caption caption = new Caption();
        caption.setId("caption1");
        caption.setLanguage("es");
        caption.setLink("https://example.com/caption1");

        when(service.getCaptionById("caption1")).thenReturn(Optional.of(caption));

        mockMvc.perform(get("/captions/caption1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("caption1"))
                .andExpect(jsonPath("$.language").value("es"));
    }

    @Test
    void testGetCaptionByIdNotFound() throws Exception {
        System.out.println("Test para comprobar que devuelve 404 si la caption no existe");

        when(service.getCaptionById("caption_falsa")).thenReturn(Optional.empty());

        mockMvc.perform(get("/captions/caption_falsa"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCaptionsByVideoId() throws Exception {
        System.out.println("Test para comprobar que se devuelven las captions de un video");

        Caption caption1 = new Caption();
        caption1.setId("caption1");
        caption1.setLanguage("es");
        caption1.setLink("https://example.com/caption1");
        caption1.setVideoId("video1");

        Caption caption2 = new Caption();
        caption2.setId("caption2");
        caption2.setLanguage("en");
        caption2.setLink("https://example.com/caption2");
        caption2.setVideoId("video1");

        when(service.getCaptionsByVideoId("video1")).thenReturn(Optional.of(Arrays.asList(caption1, caption2)));

        mockMvc.perform(get("/captions/video/video1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("caption1"))
                .andExpect(jsonPath("$[0].language").value("es"))
                .andExpect(jsonPath("$[1].id").value("caption2"))
                .andExpect(jsonPath("$[1].language").value("en"));
    }

    @Test
    void testGetCaptionsByVideoIdNotFound() throws Exception {
        System.out.println("Test para comprobar que devuelve 404 si no encuentra captions para el video");

        when(service.getCaptionsByVideoId("video_falso")).thenReturn(Optional.empty());

        mockMvc.perform(get("/captions/video/video_falso"))
                .andExpect(status().isNotFound());
    }
}