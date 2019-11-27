package com.damnteam.letmechat.data;

import com.damnteam.letmechat.data.dao.PrivilegeRepository;
import com.damnteam.letmechat.data.dao.RoleRepository;
import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.model.Privilege;
import com.damnteam.letmechat.data.model.Role;
import com.damnteam.letmechat.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

    @Override
    @EventListener
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (userRepository.findByLogin("user") == null) {
            userRepository.save(new User("user", passwordEncoder.encode("password")));
        }
        if (privilegeRepository.findByName("ALL") == null) {
            var privilege = new Privilege();
            privilege.setName("ALL");
            privilegeRepository.save(privilege);
        }
        if (roleRepository.findByName("USER") == null) {
            var role = new Role();
            role.setName("USER");
            role.setPrivileges(Arrays.asList(privilegeRepository.findByName("ALL")));
            roleRepository.save(role);
        }
    }
}
