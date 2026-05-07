package aiss.videominer.controller;

import aiss.videominer.model.User;
import aiss.videominer.service.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void testGetAllUsers() throws Exception {
        System.out.println("Test para comprobar que se devuelven todos los usuarios");

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Usuario 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Usuario 2");

        when(service.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Usuario 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Usuario 2"));
    }

    @Test
    void testGetUserById() throws Exception {
        System.out.println("Test para comprobar que se devuelve un usuario por su id");

        User user = new User();
        user.setId(1L);
        user.setName("Usuario 1");

        when(service.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Usuario 1"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        System.out.println("Test para comprobar que devuelve 404 si el usuario no existe");

        when(service.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }
}