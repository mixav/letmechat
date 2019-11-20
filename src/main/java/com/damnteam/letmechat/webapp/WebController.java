package com.damnteam.letmechat.webapp;

import com.damnteam.letmechat.data.User;
import com.damnteam.letmechat.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;


@Controller
public class WebController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String userInfo(@PathVariable("id") Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().toString();
        } else throw new Exception("User not found");
    }
}
