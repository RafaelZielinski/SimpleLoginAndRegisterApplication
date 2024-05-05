package pl.zielinski.SimpleLoginAndRegisterApplication.repository;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;

import java.util.Collection;

/**
 * @author rafek
 * @version 1.0
 * @licence free
 * @since 2023}-12-22
 */
public interface UserRepository<T extends User> {
    T create(T data, String verificationUrl);

    Collection<T> list();

    T get(Long id);

    T update(T data);

    T verifyAccountKey(String key);

    User getUserByEmail(String email);

    void sendVerificationCode(UserDTO user, String verificationCode);

    User verifyMfaCode(String email, String code);
}
