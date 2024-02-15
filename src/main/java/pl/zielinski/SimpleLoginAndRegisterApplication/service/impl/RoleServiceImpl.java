package pl.zielinski.SimpleLoginAndRegisterApplication.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.mapper.RoleDTOMapper;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/01/2024
 */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository<Role> repository;


    @Override
    public RoleDTO getRoleByUserId(Long id) {
        return RoleDTOMapper.fromRole(repository.getRoleByUserId(id));
    }

    @Override
    public Collection<RoleDTO> getRoles() {
        return (repository.list().stream().map(RoleDTOMapper::fromRole).collect(Collectors.toList()));
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        return RoleDTOMapper.fromRole(repository.get(id));
    }
}
