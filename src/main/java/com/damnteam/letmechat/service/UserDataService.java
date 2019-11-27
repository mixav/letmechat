package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.UserDataRepository;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.data.model.User;
import com.damnteam.letmechat.data.model.UserData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDataService {

    private final UserDataRepository userDataRepository;

    public UserDataService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Transactional
    public UserData createUserDataFromDTO(UserDTO userDTO, User user) {
        var userData = new UserData();
        userData.setFirstName(userDTO.getFirstName());
        userData.setLastName(userDTO.getLastName());
        userData.setUser(user);
        return userDataRepository.save(userData);
    }
}
