package pl.zielinski.SimpleLoginAndRegisterApplication.service;

import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;

import java.util.Collection;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/01/2024
 */
public interface RoleService {
    RoleDTO getRoleByUserId(Long id);

    Collection<RoleDTO> getRoles();
}
