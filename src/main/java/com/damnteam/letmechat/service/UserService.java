package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.RoleRepository;
import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.dto.RegistrationDTO;
import com.damnteam.letmechat.data.model.User;
import com.damnteam.letmechat.error.LoginAlreadyTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService extends GenericService<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private RoleRepository roleRepository;

    public boolean loginExists(String login) {
        return userRepository.findByName(login).isPresent();
    }

    @Transactional
    public User createUserFromDTO(RegistrationDTO registrationDTO) throws Exception {
        if (loginExists(registrationDTO.getLogin())) {
            throw new LoginAlreadyTakenException();
        }
        var user = new User();
        user.setName(registrationDTO.getLogin());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setUserData(userDataService.createUserDataFromDTO(registrationDTO, user));
        user.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findByName("USER").get())));
        return userRepository.save(user);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}
