package pl.zielinski.SimpleLoginAndRegisterApplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.mapper.UserDTOMapper;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.UserRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

import java.util.Collection;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 07/01/2024
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public Collection<UserDTO> getUsers() {
        return (userRepository.list().stream().map(UserDTOMapper::fromUser).toList());
    }

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user), roleRepository.getRoleByUserId(user.getId()));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }

    @Override
    public UserDTO updateUserData(User user) {
        return UserDTOMapper.fromUser(userRepository.update(user));
    }

    @Override
    public UserDTO getUser(Long id) {
        return UserDTOMapper.fromUser(userRepository.get(id));
    }

    @Override
    public UserDTO verifyAccountKey(String key) {
        return UserDTOMapper.fromUser(userRepository.verifyAccountKey(key));
    }
}
