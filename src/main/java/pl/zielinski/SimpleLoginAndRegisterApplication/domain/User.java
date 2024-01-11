package pl.zielinski.SimpleLoginAndRegisterApplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author rafek
 * @version 1.0
 * @licence free
 * @since 2023}-12-22
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@SuperBuilder
@ToString
public class User {
    private Long id;
    @NotEmpty(message = "First name can not be empty")
    private String firstName;
    @NotEmpty(message = "Last name can not be empty")
    private String lastName;
    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;
    private Long age;
    @NotEmpty(message = "Password name can not be empty")
    private String password;
    private Boolean enabled;
    private Boolean isNotLocked;
    private Boolean isUsingMfa;
    private LocalDateTime createdAt;
}
