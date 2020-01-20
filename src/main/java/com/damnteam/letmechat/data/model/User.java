package com.damnteam.letmechat.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserData userData;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    private Collection<Channel> createdChannels;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<Channel> ownedChannels;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "subscribers")
    private Collection<Channel> subscriptions;

    private boolean online = false;
}
