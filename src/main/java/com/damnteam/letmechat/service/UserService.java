package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.model.User;
import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.error.LoginAlreadyTakenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserDataService userDataService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDataService userDataService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDataService = userDataService;
    }

    public boolean loginExists(String login) {
        return userRepository.findByLogin(login) != null;
    }

    public User createUserFromDTO(UserDTO userDTO) throws Exception {
        if (loginExists(userDTO.getLogin())) {
            throw new LoginAlreadyTakenException();
        }
        var user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserData(userDataService.createUserDataFromDTO(userDTO, user));
        return userRepository.save(user);
    }
}
