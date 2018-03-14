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
    CommentRepository commentRepository;

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
        return "redirect:/showallposts";
    }
    //Add comments
    @RequestMapping(value="/commentonpost/{id}",method=RequestMethod.GET)
    public String commentOnPost(@PathVariable("id") long id, Model model){
        model.addAttribute("post", postRepository.findOne(id));
        model.addAttribute("comment",new Comment());
        return "commentform";
    }
    @RequestMapping(value="/commentform",method= RequestMethod.POST)
    public String processCommentPost(@Valid @ModelAttribute("post") Post post,@Valid @ModelAttribute("comment") Comment comment, BindingResult result,User user,Authentication authentication,
                                  RedirectAttributes redirectAttributes){

        if(result.hasErrors()){
            return "commentform";
        }else{
            user=userRepository.findByUsername(authentication.getName());

            comment.setPost(post);
            comment.setUser(user);
            post.setCommentCount(post.getCommentCount()+1);
            Date date = new Date();
            //String strDateFormat = "hh:mm:ss a";
            String strDateFormat = "h:mm - MMM d, yyyy";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate= dateFormat.format(date);
            comment.setCommentDate(formattedDate);
            postRepository.save(post);
            commentRepository.save(comment);
        }
        return "redirect:/showallposts";
    }
    //-------------------------display----------------------------------
    //Show user profile
    @RequestMapping("/userprofile")
    public String showProfile(Model model,Authentication auth,User user)
    {
        user = userRepository.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "userprofile";
    }
    //Show user posts
    @RequestMapping("/showpost")
    public String showPost(Model model,Authentication auth,User user)
    {
        user = userRepository.findByUsername(auth.getName());
        model.addAttribute("post", postRepository.findByUser(user));
        return "showpost";
    }
    //Show users list
    @RequestMapping("/userslist")
    public String showUsersList(Model model)
    {
        model.addAttribute("user", userRepository.findAll());
        return "userslist";
    }

    //Show all posts
    @RequestMapping("/showallposts")
    public String ShowAllPosts(Model model,Authentication auth,User user,Post post)
    {
        user=userRepository.findByUsername(auth.getName());

        Collection<User> users=user.getFollowing();


        for (User u :
                users) {
            model.addAttribute("post", postRepository.findByUser(u));
            Collection<Post> posts=postRepository.findByUser(u);
            for(Post p :
                   posts ){
                model.addAttribute("comment",commentRepository.findByPost(p));
            }

        }
        //To show self post
        //model.addAttribute("post", postRepository.findByUser(user));
        //To show all posts
        //model.addAttribute("post", postRepository.findAll());
        return "showallposts";
    }
    //----------------------- Edit -------------------------------------
    @RequestMapping(value="/edituserprofile/{id}",method=RequestMethod.GET)
    public String editProfile(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userRepository.findOne(id));
        return "registration";
    }
    @RequestMapping("/follow/{id}")
    public String userFollowsUsers(@PathVariable("id") long id,Authentication authentication, Model model){
        User followingUser=userRepository.findByUsername(authentication.getName());
        User followedUser=userRepository.findById(id);
        followedUser.setFollowersCount(followedUser.getFollowersCount()+1);
        //Add followed user to the follower list
        Collection<User> following=new HashSet<>();
        following.add(followedUser);
        followingUser.setFollowing(following);
        //Add following user to the list of followed users
        Collection<User> followed=new HashSet<>();
        followed.add(followingUser);
        followedUser.setFollowers(followed);
        userRepository.save(followedUser);
        userRepository.save(followingUser);
        model.addAttribute("user", userRepository.findAll());
        return "redirect:/userslist";
    }

    @RequestMapping(value="/showfollowers/{id}",method=RequestMethod.GET)
    public String showFollowers(@PathVariable("id") long id, Model model){
        User user=userRepository.findOne(id);
        model.addAttribute("user", userRepository.findByFollowing(user));
        return "userslist";
    }

    @RequestMapping("/likepost/{id}")
    public String likePost(@PathVariable("id") long id,Authentication authentication, Model model){
        Post post=postRepository.findOne(id);
        //User user=userRepository.findByUsername(authentication.getName());
        //post.setUser(user);
        post.setLikeCount(post.getLikeCount()+1);
        postRepository.save(post);
        //model.addAttribute("post", postRepository.findAll());
        return "redirect:/showallposts";
    }


}
