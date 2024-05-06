package pl.zielinski.SimpleLoginAndRegisterApplication.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.mapper.UserDTOMapper;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.UserRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.EmailService;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.SmsService;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

import java.util.Collection;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static pl.zielinski.SimpleLoginAndRegisterApplication.enumeration.VerificationType.ACCOUNT;
import static pl.zielinski.SimpleLoginAndRegisterApplication.mapper.UserDTOMapper.fromUser;
import static pl.zielinski.SimpleLoginAndRegisterApplication.utils.PathProvider.getVerificationUrl;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 07/01/2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;
    private final SmsService smsService;
    private final EmailService emailService;

    @Override
    public Collection<UserDTO> getUsers() {
        return (userRepository.list().stream().map(UserDTOMapper::fromUser).toList());
    }

    @Override
    public UserDTO createUser(User user) {

        UserDTO userDTO = fromUser(userRepository.create(user), roleRepository.getRoleByUserId(user.getId()));
        emailService.sendSimpleMailMessage(userDTO.getEmail(), userDTO.getFirstName(),  "hello");
        return userDTO;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return fromUser(userRepository.getUserByEmail(email));
    }

    @Override
    public UserDTO updateUserData(User user) {
        return fromUser(userRepository.update(user));
    }

    @Override
    public UserDTO getUser(Long id) {
        return fromUser(userRepository.get(id));
    }

    @Override
    public UserDTO verifyAccountKey(String key)
    {
        return fromUser(userRepository.verifyAccountKey(key));
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        String verificationCode = randomAlphabetic(7).toUpperCase();
        userRepository.sendVerificationCode(user, verificationCode);
        smsService.sendSms(user.getPhone(), "From Rafael Zet \nVerification Code \n" + verificationCode);
    }

    @Override
    public UserDTO verifyMfaCode(String email, String code) {
        return fromUser(userRepository.verifyMfaCode(email, code));
    }


}
