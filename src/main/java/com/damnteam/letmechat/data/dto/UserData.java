package com.damnteam.letmechat.data.dto;


import com.damnteam.letmechat.data.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;


@Data
public class UserData {

    private String username;


    private String password;


    private String firstName;


    private String lastName;


    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                username,
                passwordEncoder.encode(password),
                firstName,
                lastName);
    }
}
