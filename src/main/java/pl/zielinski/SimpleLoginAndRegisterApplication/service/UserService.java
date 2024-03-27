package pl.zielinski.SimpleLoginAndRegisterApplication.service;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;

import java.util.Collection;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 07/01/2024
 */
public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
    UserDTO updateUserData(User user);
    Collection<UserDTO> getUsers();
    UserDTO getUser(Long id);
    UserDTO verifyAccountKey(String key);
}
