package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.service.CommentService;
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

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService service;

    @Test
    void testGetAllComments() throws Exception {
        System.out.println("Test para comprobar que se devuelven todos los comentarios");

        Comment comment1 = new Comment();
        comment1.setId("comment1");
        comment1.setText("Comentario 1");

        Comment comment2 = new Comment();
        comment2.setId("comment2");
        comment2.setText("Comentario 2");

        when(service.getAllComments()).thenReturn(Arrays.asList(comment1, comment2));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("comment1"))
                .andExpect(jsonPath("$[0].text").value("Comentario 1"))
                .andExpect(jsonPath("$[1].id").value("comment2"))
                .andExpect(jsonPath("$[1].text").value("Comentario 2"));
    }

    @Test
    void testGetCommentById() throws Exception {
        System.out.println("Test para comprobar que se devuelve un comentario por su id");

        Comment comment = new Comment();
        comment.setId("comment1");
        comment.setText("Comentario 1");

        when(service.getCommentById("comment1")).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/comments/comment1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("comment1"))
                .andExpect(jsonPath("$.text").value("Comentario 1"));
    }

    @Test
    void testGetCommentByIdNotFound() throws Exception {
        System.out.println("Test para comprobar que devuelve 404 si el comentario no existe");

        when(service.getCommentById("comment_falso")).thenReturn(Optional.empty());

        mockMvc.perform(get("/comments/comment_falso"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCommentsByVideoId() throws Exception {
        System.out.println("Test para comprobar que se devuelven los comentarios de un video");

        Comment comment1 = new Comment();
        comment1.setId("comment1");
        comment1.setText("Comentario 1");
        comment1.setVideoId("video1");

        Comment comment2 = new Comment();
        comment2.setId("comment2");
        comment2.setText("Comentario 2");
        comment2.setVideoId("video1");

        when(service.getCommentsByVideoId("video1")).thenReturn(Optional.of(Arrays.asList(comment1, comment2)));

        mockMvc.perform(get("/comments/video/video1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("comment1"))
                .andExpect(jsonPath("$[0].text").value("Comentario 1"))
                .andExpect(jsonPath("$[1].id").value("comment2"))
                .andExpect(jsonPath("$[1].text").value("Comentario 2"));
    }

    @Test
    void testGetCommentsByVideoIdNotFound() throws Exception {
        System.out.println("Test para comprobar que devuelve 404 si no encuentra comentarios para el video");

        when(service.getCommentsByVideoId("video_falso")).thenReturn(Optional.empty());

        mockMvc.perform(get("/comments/video/video_falso"))
                .andExpect(status().isNotFound());
    }
}