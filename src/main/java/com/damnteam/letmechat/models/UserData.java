package com.damnteam.letmechat.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserData {

    @Column
    private String firstName;

    @Column
    private String lastName;

    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;
}
