package pl.zielinski.SimpleLoginAndRegisterApplication.mapper;

import org.springframework.beans.BeanUtils;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/01/2024
 */
public class RoleDTOMapper {
    public static RoleDTO fromRole(Role role) {
        RoleDTO roleDTOMapper = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTOMapper);
        return roleDTOMapper;

    }
}
