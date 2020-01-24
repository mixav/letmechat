package com.damnteam.letmechat.service;

import com.damnteam.letmechat.data.dao.RoleRepository;
import com.damnteam.letmechat.data.dao.UserRepository;
import com.damnteam.letmechat.data.dto.RegistrationDTO;
import com.damnteam.letmechat.data.dto.UserDTO;
import com.damnteam.letmechat.data.model.User;
import com.damnteam.letmechat.error.LoginAlreadyTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@Service
public class UserService extends GenericService<User> {

    @Autowired
    SimpUserRegistry simpUserRegistry;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SimpMessagingTemplate webSocket;

    public boolean loginExists(String login) {
        return userRepository.findByName(login).isPresent();
    }

    @Transactional
    public User createUserFromDTO(RegistrationDTO registrationDTO) throws Exception {
        if (loginExists(registrationDTO.getLogin())) {
            throw new LoginAlreadyTakenException();
        }
        var user = new User();
        user.setName(registrationDTO.getLogin());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setUserData(userDataService.createUserDataFromDTO(registrationDTO, user));
        user.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findByName("USER").get())));
        return userRepository.save(user);
    }

    public User findByName(String name) throws Exception {
        var user = userRepository.findByName(name);
        if (user.isPresent())
            return user.get();
        throw new Exception("User not found.");
    }

    public boolean checkOnline(User user) {
        var users = simpUserRegistry.getUsers();
        return users.stream().anyMatch(simpUser -> simpUser.getName().equals(user.getName()));
    }

    @EventListener
    @Transactional
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        var discUser = event.getUser();
        if (discUser != null) {
            var res = userRepository.findByName(discUser.getName());
            if (res.isPresent()) {
                var user = res.get();
                var head = new HashMap<String, Object>();
                head.put("message-type", "notification");
                head.put("notification-type", "user-offline");
                simpUserRegistry.findSubscriptions(subscription -> subscription.getSession().getId().equals(event.getSessionId()))
                        .forEach(subscription -> webSocket.convertAndSend(subscription.getDestination(), new UserDTO(user).setOnline(false), head));
            }
        }
    }

    @EventListener
    @Transactional
    public void onSessionSubscribed(SessionSubscribeEvent event) {
        var subUser = event.getUser();
        var destination = (String) event.getMessage().getHeaders().get("simpDestination");
        if (subUser != null && destination != null) {
            var res = userRepository.findByName(subUser.getName());
            if (res.isPresent()) {
                var user = res.get();
                var head = new HashMap<String, Object>();
                head.put("message-type", "notification");
                head.put("notification-type", "update-user");
                webSocket.convertAndSend(destination, new UserDTO(user).setOnline(true), head);
            }
        }
    }

    @EventListener
    @Transactional
    public void onSessionUnsubscribed(SessionUnsubscribeEvent event) {
        var subUser = event.getUser();
        var id = (String) event.getMessage().getHeaders().get("simpSubscriptionId");
        if (subUser != null && id != null) {
            var res = userRepository.findByName(subUser.getName());
            if (res.isPresent()) {
                var user = res.get();
                var head = new HashMap<String, Object>();
                head.put("message-type", "notification");
                head.put("notification-type", "remove-user");
                webSocket.convertAndSend(id, new UserDTO(user).setOnline(false), head);
            }
        }
    }
}
