package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import aiss.videominer.service.ChannelService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelService service;

    @Test
    void testGetAllChannels() throws Exception {
        System.out.println("Test para comprobar que se devuelven todos los canales");

        Channel channel1 = new Channel();
        channel1.setId("channel1");
        channel1.setName("Canal 1");

        Channel channel2 = new Channel();
        channel2.setId("channel2");
        channel2.setName("Canal 2");

        when(service.getAllChannels()).thenReturn(Arrays.asList(channel1, channel2));

        mockMvc.perform(get("/channels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("channel1"))
                .andExpect(jsonPath("$[0].name").value("Canal 1"))
                .andExpect(jsonPath("$[1].id").value("channel2"))
                .andExpect(jsonPath("$[1].name").value("Canal 2"));
    }

    @Test
    void testGetChannelById() throws Exception {
        System.out.println("Test para comprobar que se devuelve un canal por su id");

        Channel channel = new Channel();
        channel.setId("channel1");
        channel.setName("Canal 1");

        when(service.getChannelById("channel1")).thenReturn(Optional.of(channel));

        mockMvc.perform(get("/channels/channel1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("channel1"))
                .andExpect(jsonPath("$.name").value("Canal 1"));
    }

    @Test
    void testGetChannelByIdNotFound() throws Exception {
        System.out.println("Test para comprobar que devuelve 404 si el canal no existe");

        when(service.getChannelById("channel_falso")).thenReturn(Optional.empty());

        mockMvc.perform(get("/channels/channel_falso"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateChannel() throws Exception {
        System.out.println("Test para comprobar que se puede crear un canal");

        Channel channel = new Channel();
        channel.setId("channel1");
        channel.setName("Canal 1");

        when(service.saveChannel(ArgumentMatchers.any(Channel.class))).thenReturn(channel);

        String body = """
                {
                    "id": "channel1",
                    "name": "Canal 1"
                }
                """;

        mockMvc.perform(post("/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("channel1"))
                .andExpect(jsonPath("$.name").value("Canal 1"));
    }
}