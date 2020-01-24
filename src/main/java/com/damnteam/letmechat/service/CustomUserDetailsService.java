package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.model.Privilege;
import com.damnteam.letmechat.data.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var optionalUser = userRepository.findByName(login);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("No user found for login: " + login);
        }
        var user = optionalUser.get();
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
