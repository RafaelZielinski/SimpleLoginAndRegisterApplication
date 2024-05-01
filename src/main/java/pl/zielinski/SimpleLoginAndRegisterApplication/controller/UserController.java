package pl.zielinski.SimpleLoginAndRegisterApplication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.HttpResponse;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.UserPrincipal;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.form.LoginForm;
import pl.zielinski.SimpleLoginAndRegisterApplication.provider.TokenProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static pl.zielinski.SimpleLoginAndRegisterApplication.mapper.RoleDTOMapper.toRole;
import static pl.zielinski.SimpleLoginAndRegisterApplication.mapper.UserDTOMapper.toUser;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RoleService roleService;

    @PostMapping("/login")
    ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(unauthenticated(loginForm.getEmail(), loginForm.getPassword()));
        UserDTO loggedInUser = ((UserPrincipal) authentication.getPrincipal()).getUserDTO();
        return loggedInUser.isUsingMfa() ? sendVerificationCode(loggedInUser) : sendResponse(loggedInUser);
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO loggedInUser) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login Success")
                        .data(Map.of("user", loggedInUser, "access_token", tokenProvider.createAccessToken(getUserPrincipal(loggedInUser)), "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(loggedInUser))))
                        .build(), HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("Verification Code Sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private UserPrincipal getUserPrincipal(UserDTO user) {
        return new UserPrincipal(toUser(userService.getUserByEmail(user.getEmail())), toRole(roleService.getRoleByUserId(user.getId())));
    }

    @PostMapping("/register")
    ResponseEntity<HttpResponse> register(@RequestBody @Valid User user) {
        UserDTO userDTO = userService.createUser(user);
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .message("User created")
                        .data(Map.of("user", userDTO))
                        .build(), CREATED);


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

    //verify your account, set true to enable without authorization only url, probably send to email
    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable String key) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message(userService.verifyAccountKey(key).isEnabled() ? "Account already verified" : "Account verified")
                        .status(HttpStatus.OK)
                        .statusCode(OK.value())
                        .build());

    }


}
