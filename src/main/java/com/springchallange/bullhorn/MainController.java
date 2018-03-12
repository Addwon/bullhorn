package com.springchallange.bullhorn;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/")
    public String showIndex(Model model)
    {
       return "index";
    }

/*    //User registration
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

    }*/

    //For user registration
    @RequestMapping(value="/registration",method=RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user",new User());
        return "registration";
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, @RequestParam("file")MultipartFile file, BindingResult result,
                                          RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "registration";
        }
        if(file.isEmpty()){
            return "registration";
        }
        try{
            Map uploadResult=cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype","auto"));
            user.setUserImageUrl(uploadResult.get("url").toString());
            //actorRepository.save(actor);
        }catch (IOException e){
            e.printStackTrace();
            return "registration";
        }
//        model.addAttribute("user",user);
        if(result.hasErrors()){
            return "registration";
        }else{
            user.setEnabled(true);
            //Role role =roleRepository.findByRole("USER");
            user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));

            System.out.println("user reg.post[role.getRole(): "+user.getRoles());
            //user.setRoles(role.getRole());
            userRepository.save(user);
//            model.addAttribute("message","User Account Successfully Created");
        }
        return "redirect:/";
    }

    @RequestMapping("/userprofile")
    public String showFilteredNews(Model model,Authentication auth,User user)
    {
        user = userRepository.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "userprofile";
    }
}
