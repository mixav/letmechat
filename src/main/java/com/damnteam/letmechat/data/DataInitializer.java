package com.damnteam.letmechat.data;

import com.damnteam.letmechat.data.dao.PrivilegeRepository;
import com.damnteam.letmechat.data.dao.RoleRepository;
import com.damnteam.letmechat.data.dao.UserDataRepository;
import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.model.Privilege;
import com.damnteam.letmechat.data.model.Role;
import com.damnteam.letmechat.data.model.User;
import com.damnteam.letmechat.data.model.UserData;
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

    public DataInitializer(UserRepository userRepository, PrivilegeRepository privilegeRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDataRepository = userDataRepository;
    }

    @Override
    @EventListener
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (privilegeRepository.findByName("ROLE_USER") == null) {
            var privilege = new Privilege();
            privilege.setName("ROLE_USER");
            privilegeRepository.save(privilege);
        }
        if (roleRepository.findByName("USER") == null) {
            var role = new Role();
            role.setName("USER");
            role.setPrivileges(Collections.singletonList(privilegeRepository.findByName("ROLE_USER")));
            roleRepository.save(role);
        }
        User user = userRepository.findByLogin("user");
        if (user == null) {
            user = new User();
            user.setLogin("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
            userRepository.save(user);
        }
        if (userDataRepository.findByUser(user) == null) {
            UserData userData = new UserData();
            userData.setFirstName("admin");
            userData.setLastName("admin");
            userData.setUser(user);
            userDataRepository.save(userData);
        }
    }
}
