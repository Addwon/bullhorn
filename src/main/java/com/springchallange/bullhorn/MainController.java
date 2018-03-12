package com.springchallange.bullhorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/")
    public String showIndex(Model model)
    {
       return "index";
    }

    //User registration
    @RequestMapping(value="/registration",method= RequestMethod.GET)
    public String showRegistrationPage(Model model){
        //model.addAttribute("classActiveSettings6","nav-item active");
        model.addAttribute("user",new User());
        return "registration";
    }
    @RequestMapping(value="/registration",method= RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registration";
        }else{
            user.setEnabled(true);
            //Role role =roleRepository.findByRole("USER");
            user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));

            System.out.println("user reg.post[role.getRole(): "+user.getRoles());
            //user.setRoles(role.getRole());

            userRepository.save(user);
            return "redirect:/";
        }

    }


}
