package pl.zielinski.SimpleLoginAndRegisterApplication.mapper;

import org.springframework.beans.BeanUtils;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 07/01/2024
 */
public class UserDTOMapper {

    public static UserDTO fromUser(User user) {
        UserDTO userDTOMapper = new UserDTO();
        BeanUtils.copyProperties(user, userDTOMapper);
        return userDTOMapper;
    }
    public static UserDTO fromUser(User user, Role role) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleName(role.getName());
        userDTO.setPermissions(role.getPermission());
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
