package pl.zielinski.SimpleLoginAndRegisterApplication.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.HttpResponse;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 03/01/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping(path = "/list")
    ResponseEntity<HttpResponse> getRoles() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("List of roles")
                        .data(Map.of("roles", roleService.getRoles()))
                        .build()
        );
    }

    @GetMapping(path = "/role/{id}")
    ResponseEntity<HttpResponse> getRoleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Role retrieved")
                        .data(Map.of("role", roleService.getRoleById(id)))
                        .build()
        );
    }

    @GetMapping(path = "/roleByUserId//{userId}")
    ResponseEntity<HttpResponse> getRoleByUserId(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Role by user id retrieved")
                        .data(Map.of("role", roleService.getRoleByUserId(id)))
                        .build()
        );
    }
}
