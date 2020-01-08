package com.damnteam.letmechat.data;

import com.damnteam.letmechat.data.dao.*;
import com.damnteam.letmechat.data.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    @EventListener
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        var user = userRepository.findByName("user").orElseGet(this::createUserIfNotExists);
        createUserDataIfNotExists(user);
        createChannelIfNotExists(user);
    }

    private void createChannelIfNotExists(User user) {
        if (channelRepository.findByName("Main").isEmpty()) {
            var channel = new Channel();
            channel.setCreator(user);
            channel.setOwner(user);
            channel.setName("Main");
            channelRepository.save(channel);
        }
        if (channelRepository.findByName("Add").isEmpty()) {
            var channel = new Channel();
            channel.setCreator(user);
            channel.setOwner(user);
            channel.setName("Add");
            channelRepository.save(channel);
        }
        if (channelRepository.findByName("Sub").isEmpty()) {
            var channel = new Channel();
            channel.setCreator(user);
            channel.setOwner(user);
            channel.setName("Sub");
            channelRepository.save(channel);
        }
    }

    private void createUserDataIfNotExists(User user) {
        if (userDataRepository.findByUser(user).isEmpty()) {
            var userData = new UserData();
            userData.setFirstName("admin");
            userData.setLastName("admin");
            userData.setUser(user);
            userDataRepository.save(userData);
        }
    }

    private User createUserIfNotExists() {
        var user = new User();
        user.setName("user");
        user.setPassword(passwordEncoder.encode("password"));
        var roles = roleRepository.findByName("USER").orElseGet(this::createRoleIfNotExists);
        user.setRoles(Collections.singletonList(roles));
        return userRepository.save(user);
    }

    private Role createRoleIfNotExists() {
        var role = new Role();
        role.setName("USER");
        var privilege = privilegeRepository.findByName("ROLE_USER").orElseGet(this::createPrivilegeIfNotExists);
        role.setPrivileges(Collections.singleton(privilege));
        return roleRepository.save(role);
    }

    private Privilege createPrivilegeIfNotExists() {
        var privilege = new Privilege();
        privilege.setName("ROLE_USER");
        return privilegeRepository.save(privilege);
    }
}
