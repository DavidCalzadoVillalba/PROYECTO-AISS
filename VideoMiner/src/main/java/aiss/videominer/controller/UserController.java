package aiss.videominer.controller;

import aiss.videominer.model.User;
import aiss.videominer.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios (VideoMiner)", description = "Operaciones de consulta para los usuarios en la base de datos")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Devuelve una lista con todos los usuarios guardados en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Lista devuelta correctamente")
    public List<User> findAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar un usuario por su id", description = "Devuelve un usuario previamente guardado en la base de datos H2.")
    @ApiResponse(responseCode = "200", description = "Usuario devuelto correctamente")
    public ResponseEntity<User> findOne(@PathVariable Long id) {
        Optional<User> user = service.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }
}
