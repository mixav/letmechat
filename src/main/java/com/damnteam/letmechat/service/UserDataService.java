package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.*;
import com.damnteam.letmechat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public UserData createUserDataFromDTO(UserDTO userDTO, User user) {
        var userData = new UserData();
        userData.setFirstName(userDTO.getFirstName());
        userData.setLastName(userDTO.getLastName());
        userData.setUser(user);
        return userDataRepository.save(userData);
    }
}
