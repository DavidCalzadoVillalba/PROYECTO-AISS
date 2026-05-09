package aiss.videominer.service;

import aiss.videominer.model.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChannelServiceTests {

    @Autowired
    ChannelService service; 

    @Test
    @DisplayName("Guardar y recuperar un canal de la Base de Datos local")
    void testSaveAndRetrieveChannel() {
        System.out.println("EJECUTANDO TEST: Transacción de BD local (Guardar y Leer)...");

        // 1. Preparar los datos falsos en memoria
        Channel testChannel = new Channel();
        testChannel.setId("test_bd_001");
        testChannel.setName("Canal de Arquitectura Software");
        testChannel.setDescription("Datos de prueba para H2");
        testChannel.setCreatedTime("2026-05-09T17:55:06Z"); 

        // 2. Ejecutar las acciones en el servicio
        Channel savedChannel = service.saveChannel(testChannel);

        // Si tu método de buscar devuelve Optional (lo normal en Spring Data):
        Optional<Channel> retrievedChannelOpt = service.getChannelById("test_bd_001"); 

        // 3. Comprobar que todo ha ido perfecto
        assertNotNull(savedChannel, "El método de guardar no debe devolver nulo");
        assertTrue(retrievedChannelOpt.isPresent(), "El canal debe existir en la BD al buscarlo por su ID");
        
        Channel retrievedChannel = retrievedChannelOpt.get();
        assertEquals("Canal de Arquitectura Software", retrievedChannel.getName(), "El nombre recuperado debe coincidir");
    }
}