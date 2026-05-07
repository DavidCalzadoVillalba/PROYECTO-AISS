package aiss.videominer.controller;

import aiss.videominer.model.Video;
import aiss.videominer.service.VideoService;
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

@WebMvcTest(VideoController.class)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService service;

    @Test
    void testGetAllVideos() throws Exception {
        System.out.println("Test para comprobar que se devuelven todos los videos");

        Video video1 = new Video();
        video1.setId("video1");
        video1.setName("Video 1");

        Video video2 = new Video();
        video2.setId("video2");
        video2.setName("Video 2");

        when(service.getAllVideos()).thenReturn(Arrays.asList(video1, video2));

        mockMvc.perform(get("/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("video1"))
                .andExpect(jsonPath("$[0].name").value("Video 1"))
                .andExpect(jsonPath("$[1].id").value("video2"))
                .andExpect(jsonPath("$[1].name").value("Video 2"));
    }

    @Test
    void testGetVideoById() throws Exception {
        System.out.println("Test para comprobar que se devuelve un video por su id");

        Video video = new Video();
        video.setId("video1");
        video.setName("Video 1");

        when(service.getVideoById("video1")).thenReturn(Optional.of(video));

        mockMvc.perform(get("/videos/video1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("video1"))
                .andExpect(jsonPath("$.name").value("Video 1"));
    }

    @Test
    void testGetVideoByIdNotFound() throws Exception {
        System.out.println("Test para comprobar que devuelve 404 si el video no existe");

        when(service.getVideoById("video_falso")).thenReturn(Optional.empty());

        mockMvc.perform(get("/videos/video_falso"))
                .andExpect(status().isNotFound());
    }
}