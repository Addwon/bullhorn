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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostRepository postRepository;

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
    //-------------------------add--------------------------------------
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
        if(!file.isEmpty()){
            try{
      /*  if(file.isEmpty()){
            System.out.println("Image url: "+user.getUserImageUrl());
            Map uploadResult=cloudc.upload(user.getUserImageUrl().getBytes(),
                    ObjectUtils.asMap("resourcetype","auto"));
        }*/
                Map uploadResult=cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcetype","auto"));
                user.setUserImageUrl(uploadResult.get("url").toString());


            }catch (IOException e){
                e.printStackTrace();
                return "registration";
            }
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

    //Post message
    @RequestMapping(value="/postform",method=RequestMethod.GET)
    public String showPostForm(Model model){
        model.addAttribute("post",new Post());
        return "postform";
    }

    @RequestMapping(value="/postform",method= RequestMethod.POST)
    public String processPostPage(@Valid @ModelAttribute("post") Post post, @RequestParam("file")MultipartFile file, BindingResult result,User user,Authentication authentication,
                                          RedirectAttributes redirectAttributes){

        if(result.hasErrors()){
            return "postform";
        }
        if(!file.isEmpty()){
            try{

                Map uploadResult=cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcetype","auto"));
                post.setPostImageUrl(uploadResult.get("url").toString());


            }catch (IOException e){
                e.printStackTrace();
                return "postform";
            }
        }

        if(result.hasErrors()){
            return "postform";
        }else{
            user=userRepository.findByUsername(authentication.getName());
            post.setUser(user);

            Date date = new Date();
            //String strDateFormat = "hh:mm:ss a";
            String strDateFormat = "h:mm - MMM d, yyyy";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate= dateFormat.format(date);
            post.setPostDate(formattedDate);
            postRepository.save(post);

        }
        return "redirect:/";
    }
    //-------------------------display----------------------------------
    @RequestMapping("/userprofile")
    public String showFilteredNews(Model model,Authentication auth,User user)
    {
        user = userRepository.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "userprofile";
    }
    //----------------------- Edit -------------------------------------
    @RequestMapping(value="/edituserprofile/{id}",method=RequestMethod.GET)
    public String editProfile(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userRepository.findOne(id));
        return "registration";
    }

}
