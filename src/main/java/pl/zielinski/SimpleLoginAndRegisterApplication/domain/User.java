package pl.zielinski.SimpleLoginAndRegisterApplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class User {
    Long id;
    String firstName;
    String lastName;
    String email;
    String age;
    String password;
    Boolean enabled;
    Boolean isNotLocked;
    Boolean isUsingMfa;
    LocalDateTime createdAt;
}
