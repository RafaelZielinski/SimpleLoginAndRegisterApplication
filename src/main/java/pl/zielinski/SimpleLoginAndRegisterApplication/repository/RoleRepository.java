package pl.zielinski.SimpleLoginAndRegisterApplication.repository;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;

import java.util.Collection;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 25/12/2023
 */
public interface RoleRepository<T extends Role> {

    T create(T data);

    Collection<T> list();

    T get(Long id);

    T update(Long id);

    Boolean delete(Long id);

    void addRoleToUser(Long userId, String roleName);

    Role getRoleByUserId(Long userId);


}
