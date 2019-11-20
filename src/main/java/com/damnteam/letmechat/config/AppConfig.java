package com.damnteam.letmechat.config;

import com.damnteam.letmechat.data.User;
import com.damnteam.letmechat.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    public void initData(UserRepository userRepository) {
        //TODO put data from bd to file for persistence and implement userDetailsService for authorization
        userRepository.save(new User("user", "password"));
        userRepository.save(new User("user2", "password"));
    }
}
