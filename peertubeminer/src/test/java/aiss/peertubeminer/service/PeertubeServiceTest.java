package aiss.peertubeminer.service;

import aiss.peertubeminer.exception.PeertubeNotFoundException;
import aiss.peertubeminer.model.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PeerTubeServiceTests {

    @Autowired
    ChannelService service;

    @Test
    void testGetChannelExists() throws PeertubeNotFoundException {
        System.out.println("EJECUTANDO TEST: Comprobando que se puede obtener un canal real de PeerTube...");
        
        // Probamos con el canal de Blender
        Channel channel = service.getChannel("blender_open_movies", 2, 2);
        
        assertNotNull(channel, "El canal devuelto no debería ser null");
        assertEquals("9", channel.getId(), "El ID del canal debería ser el 9 en PeerTube");
        assertNotNull(channel.getName(), "El nombre del canal no debería ser null");
        assertFalse(channel.getVideos().isEmpty(), "La lista de vídeos NO debería estar vacía");
    }

    @Test
    void testGetChannelNotExists() throws PeertubeNotFoundException {
        System.out.println("EJECUTANDO TEST: Comprobando que el servicio maneja bien un canal falso...");
        // Si tu servicio devuelve null al fallar (como hicimos antes):
        // assertNull(channel, "El canal debería ser null porque no existe");
        
        // Probamos con un ID que sabemos que no existe
        assertThrows(PeertubeNotFoundException.class, () -> service.getChannel("este_canal_es_falso_123", 1, 1));
    }
}