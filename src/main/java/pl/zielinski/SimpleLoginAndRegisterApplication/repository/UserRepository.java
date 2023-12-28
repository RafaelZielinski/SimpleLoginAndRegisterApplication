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
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T date);

    User getUserByEmail(String email);

}
