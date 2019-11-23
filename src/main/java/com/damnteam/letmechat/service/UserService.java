package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.*;
import com.damnteam.letmechat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDataService userDataService;

    public boolean loginExists(String login) {
        return userRepository.findByLogin(login) != null;
    }

    public User createUserFromDTO(UserDTO userDTO) throws Exception {
        if(loginExists(userDTO.getLogin())) {
            throw new Exception("User with this login already exists");
        }
        var user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserData(userDataService.createUserDataFromDTO(userDTO,user));
        return userRepository.save(user);
    }
}
