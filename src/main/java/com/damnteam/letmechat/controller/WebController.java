package com.damnteam.letmechat.controller;

import com.damnteam.letmechat.data.User;
import com.damnteam.letmechat.dto.UserDTO;
import com.damnteam.letmechat.data.UserRepository;
import com.damnteam.letmechat.service.UserService;
import com.damnteam.letmechat.util.GenericResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class WebController {

    private final UserRepository userRepository;

    private final UserService userService;

    public WebController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registration(@Valid UserDTO userDTO) throws Exception {
//        TODO add registration processing
        userService.createUserFromDTO(userDTO);
        return new GenericResponse("success");
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String userInfo(@PathVariable("id") Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().toString();
        } else throw new Exception("User not found");
    }
}
