package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DailymotionServiceTests {

    @Autowired
    ChannelService service;

    @Test
    void testGetChannelExists() throws aiss.dailymotionminer.exception.DailymotionNotFoundException {
        System.out.println("EJECUTANDO TEST: Comprobando que se puede obtener un canal real...");
        
        // Probamos con el canal que sabemos que existe
        Channel channel = service.getChannel("news", 2, 1);
        //pruebas para las exceptions
        assertNotNull(channel, "El canal devuelto no debería ser null");
        assertEquals("x2ox9lg", channel.getId(), "El ID del canal debería coincidir con el de Dailymotion");
        assertNotNull(channel.getName(), "El nombre del canal no debería ser null");
        assertFalse(channel.getVideos().isEmpty(), "La lista de vídeos NO debería estar vacía");
    }

    @Test
    void testGetChannelNotExists() {
        System.out.println("EJECUTANDO TEST: Comprobando que el servicio maneja bien un ID falso...");
        
        // Probamos con un ID que sabemos que no existe
        assertThrows(aiss.dailymotionminer.exception.DailymotionNotFoundException.class,
            () -> service.getChannel("prueba_no_existe_el_canal", 1, 1));
    }
}