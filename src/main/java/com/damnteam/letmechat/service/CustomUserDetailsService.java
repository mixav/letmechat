package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.model.Privilege;
import com.damnteam.letmechat.data.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("No user found for login: " + login);
        }
        //TODO add roles
        return new User(login,
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(Role::getPrivileges)
                        .flatMap(Collection::stream)
                        .map(Privilege::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }
}
