package pl.zielinski.SimpleLoginAndRegisterApplication.repository;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;

import java.util.Collection;

/**
 * @author rafek
 * @version 1.0
 * @licence free
 * @since 2023}-12-22
 */
public interface UserRepository<T extends User> {
    T create(T data);
    Collection<T> list();
    T get(Long id);
    T update(T data);

    User getUserByEmail(String email);

}
