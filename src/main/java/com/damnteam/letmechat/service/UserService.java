package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.RoleRepository;
import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.data.model.User;
import com.damnteam.letmechat.error.LoginAlreadyTakenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserDataService userDataService;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDataService userDataService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDataService = userDataService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public boolean loginExists(String login) {
        return userRepository.findByLogin(login) != null;
    }

    @Transactional
    public User createUserFromDTO(UserDTO userDTO) throws Exception {
        if (loginExists(userDTO.getLogin())) {
            throw new LoginAlreadyTakenException();
        }
        var user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserData(userDataService.createUserDataFromDTO(userDTO, user));
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
        return userRepository.save(user);
    }
}
