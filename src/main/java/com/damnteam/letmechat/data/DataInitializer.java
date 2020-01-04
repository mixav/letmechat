package com.damnteam.letmechat.data;

import com.damnteam.letmechat.data.dao.*;
import com.damnteam.letmechat.data.model.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {


    private final UserRepository userRepository;

    private final PrivilegeRepository privilegeRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserDataRepository userDataRepository;

    private final ChannelRepository channelRepository;

    public DataInitializer(UserRepository userRepository, PrivilegeRepository privilegeRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserDataRepository userDataRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDataRepository = userDataRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    @EventListener
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        createPrivilegeIfNotExists();
        createRoleIfNotExists();
        var user = createUserIfNotExists();
        createUserDataIfNotExists(user);

        createChannelIfNotExists(user);
    }

    private Channel createChannelIfNotExists(User user) {
        var channel = channelRepository.findByName("Main");
        if (channel != null) return channel;
        channel = new Channel();
        channel.setCreator(user);
        channel.setOwner(user);
        channel.setName("Main");
        return channelRepository.save(channel);
    }

    private UserData createUserDataIfNotExists(User user) {
        var userData = userDataRepository.findByUser(user);
        if (userData != null)
            return userData;
        userData = new UserData();
        userData.setFirstName("admin");
        userData.setLastName("admin");
        userData.setUser(user);
        return userDataRepository.save(userData);
    }

    private User createUserIfNotExists() {
        User user = userRepository.findByLogin("user");
        if (user != null) return user;
        user = new User();
        user.setLogin("user");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
        return userRepository.save(user);
    }

    private Role createRoleIfNotExists() {
        var role = roleRepository.findByName("USER");
        if (role != null) return role;
        role = new Role();
        role.setName("USER");
        role.setPrivileges(Collections.singletonList(privilegeRepository.findByName("ROLE_USER")));
        return roleRepository.save(role);
    }

    private Privilege createPrivilegeIfNotExists() {
        Privilege privilege = privilegeRepository.findByName("ROLE_USER");
        if (privilege != null) return privilege;
        privilege = new Privilege();
        privilege.setName("ROLE_USER");
        return privilegeRepository.save(privilege);
    }
}
