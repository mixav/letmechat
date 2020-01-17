package com.damnteam.letmechat.data.dto;

import com.damnteam.letmechat.data.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String name;

    private String firstName;

    private String lastName;

    public UserDTO(User user) {
        this.name = user.getName();
        this.firstName = user.getUserData().getFirstName();
        this.lastName = user.getUserData().getLastName();
    }
}
