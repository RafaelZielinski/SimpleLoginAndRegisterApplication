package pl.zielinski.SimpleLoginAndRegisterApplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    Long id;
    String firstName;
    String lastName;
    String email;
    Long age;
    String password;
    Boolean enabled;
    Boolean isNotLocked;
    Boolean isUsingMfa;
    LocalDateTime createdAt;
}
