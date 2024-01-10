package pl.zielinski.SimpleLoginAndRegisterApplication.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.HttpResponse;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

//    @PostMapping("/login")
//    ResponseEntity<HttpResponse> login() {
//        return ResponseEntity.ok(
//                HttpResponse.builder()
//                .timeStamp(LocalDateTime.now().toString())
//                .status(HttpStatus.OK)
//                .statusCode(HttpStatus.OK.value())
//                .message("User profile retrieved")
//                .data(Map.of("users", userService.getUser()))
//                .build());
//        HttpResponse.builder()
//    }

    @PostMapping("/register")
    ResponseEntity<HttpResponse> login(@RequestBody @Valid User user) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("User created")
                .data(Map.of("user", userService.createUser(user)))
                .build());
    }

    @GetMapping("/list")
    ResponseEntity<HttpResponse> getUsers() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("List of users")
                        .data(Map.of("users", userService.getUsers()))
                        .build());
    }

    @GetMapping("/user/{id}")
    ResponseEntity<HttpResponse> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("User retrieved")
                        .data(Map.of("user", userService.getUser(id)))
                        .build());
    }


}
