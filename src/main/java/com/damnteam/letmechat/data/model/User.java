package com.damnteam.letmechat.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, login='%s']",
                id, login);
    }

    @OneToOne
    private UserData userData;
}
