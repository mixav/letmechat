package com.damnteam.letmechat.webapp;

import com.damnteam.letmechat.data.User;
import com.damnteam.letmechat.data.UserDTO;
import com.damnteam.letmechat.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class WebController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@Valid UserDTO user,
                                     BindingResult result, WebRequest request, Errors errors) {
        if (userRepository.findByLogin(user.getLogin()) != null)
            if (!result.hasErrors()) {

            }
//        TODO add registration processing
        return new ModelAndView("registration", "user", user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String userInfo(@PathVariable("id") Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().toString();
        } else throw new Exception("User not found");
    }
}
