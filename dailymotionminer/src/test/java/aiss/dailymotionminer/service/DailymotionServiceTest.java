package aiss.dailymotionminer.service;

import aiss.dailymotionminer.model.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DailymotionServiceTests {

    @Autowired
    DailymotionService service;

    @Test
    void testGetChannelExists() {
        System.out.println("EJECUTANDO TEST: Comprobando que se puede obtener un canal real...");
        
        // Probamos con el canal 'news' que sabemos que existe, pidiendo solo 2 vídeos para que sea rápido
        Channel channel = service.getChannel("news", 2, 1);
        
        // Estas son las "pruebas" automáticas:
        assertNotNull(channel, "El canal devuelto no debería ser null");
        assertEquals("x2ox9lg", channel.getId(), "El ID del canal debería coincidir con el de Dailymotion");
        assertNotNull(channel.getName(), "El nombre del canal no debería ser null");
        assertFalse(channel.getVideos().isEmpty(), "La lista de vídeos NO debería estar vacía");
    }

    @Test
    void testGetChannelNotExists() {
        System.out.println("EJECUTANDO TEST: Comprobando que el servicio maneja bien un ID falso...");
        
        // Probamos con un ID inventado que es imposible que exista
        Channel channel = service.getChannel("este_canal_no_existe_9999999", 1, 1);
        
        // La prueba: el servicio debería atrapar el error 404 y devolver null tranquilamente
        assertNull(channel, "El servicio debería devolver null si el canal no existe");
    }
}