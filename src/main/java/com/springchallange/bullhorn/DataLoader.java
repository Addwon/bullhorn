package com.springchallange.bullhorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public void run(String... strings) throws Exception{

       System.out.println("Loading data . . .");
       roleRepository.save(new Role("USER"));
       roleRepository.save(new Role("ADMIN"));

        Role adminRole=roleRepository.findByRole("ADMIN");
        Role userRole=roleRepository.findByRole("USER");

        User user1=new User("admin@admin.com","password","Dave","Wolf",true,"Dave");
        user1.setUserImageUrl("/images/UserImage.png");
        user1.setFollowersCount(0);
        user1.setRoles(Arrays.asList(adminRole));
        userRepository.save(user1);

        User user2=new User("user@user.com","password","Addis","Wondie",true,"Addis");
        user2.setUserImageUrl("/images/UserImage.png");
        user2.setFollowersCount(0);
        user2.setRoles(Arrays.asList(userRole));
        userRepository.save(user2);

        User user3 = new User("bob@bob.com", "password", "Bob", "Marley", true, "Bob");
        user3.setUserImageUrl("http://res.cloudinary.com/addwon/image/upload/v1520885155/jlek6zcf96kjzoakxkes.png");
        user3.setFollowersCount(0);
        user3.setRoles(Arrays.asList(userRole));
        userRepository.save(user3);

        Collection<User> u2=new HashSet<>();
        u2.add(user3);
        user2.setFollowing(u2);
        user2.setFollowingCount(user2.getFollowingCount()+1);
        u2.add(user2);
        user3.setFollowers(u2);
        user3.setFollowersCount(user3.getFollowersCount()+1);
        userRepository.save(user2);
        userRepository.save(user3);

        Date date = new Date();
        String strDateFormat = "h:mm - MMM d, yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);

        Post post1=new Post();
        post1.setPostImageUrl("http://res.cloudinary.com/addwon/image/upload/v1520976380/sk6fj5rfnpdz1a5clgzn.png");
        post1.setPostMessage("My news aggregator website has been launched, #addisnews.");
        post1.setPostDate(formattedDate);
        post1.setCommentCount(1);
        post1.setUser(user2);
        post1.getUser().setPostCount(post1.getUser().getPostCount()+1);
        postRepository.save(post1);
        userRepository.save(user2);

        Post post2=new Post();
        post2.setPostImageUrl("http://res.cloudinary.com/addwon/image/upload/v1520963208/ihvbwoia4ctj9vcsxngm.jpg");
        post2.setPostMessage("Ultimate skills #premierleague #arsenal, @MesutOzil1088");
        post2.setPostDate(formattedDate);
        post2.setUser(user3);
        post2.setCommentCount(1);
        post2.getUser().setPostCount(post2.getUser().getPostCount()+1);
        postRepository.save(post2);
        userRepository.save(user3);

        Comment comment1=new Comment();
        comment1.setCommentDate(formattedDate);
        comment1.setCommentMessage("flawless passes");
        comment1.setPost(post2);
        comment1.setUser(user2);
        commentRepository.save(comment1);

        Comment comment2=new Comment();
        comment2.setCommentDate(formattedDate);
        comment2.setCommentMessage("Well organized");
        comment2.setPost(post1);
        comment2.setUser(user3);
        commentRepository.save(comment2);
    }
}
