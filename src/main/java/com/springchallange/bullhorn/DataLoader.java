package com.springchallange.bullhorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostRepository postRepository;

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
        user1.setFollowersCount(0);
        user2.setRoles(Arrays.asList(userRole));
        userRepository.save(user2);

        User user3 = new User("bob@bob.com", "password", "Bob", "Marley", true, "Bob");
        user3.setUserImageUrl("http://res.cloudinary.com/addwon/image/upload/v1520885155/jlek6zcf96kjzoakxkes.png");
        user1.setFollowersCount(0);
        user3.setRoles(Arrays.asList(userRole));
        userRepository.save(user3);

        Date date = new Date();
        //String strDateFormat = "hh:mm:ss a";
        String strDateFormat = "h:mm - MMM d, yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);

        Post post1=new Post();
        post1.setPostImageUrl("http://res.cloudinary.com/addwon/image/upload/v1520976380/sk6fj5rfnpdz1a5clgzn.png");
        post1.setPostMessage("My news aggregator website has been launched, #addisnews.");
        post1.setPostDate(formattedDate);
        post1.setUser(user2);
        postRepository.save(post1);

        Post post2=new Post();
        post2.setPostImageUrl("http://res.cloudinary.com/addwon/image/upload/v1520963208/ihvbwoia4ctj9vcsxngm.jpg");
        post2.setPostMessage("Have you seen Mesut Özil playing? #premierleague #arsenal, @MesutOzil1088");
        post2.setPostDate(formattedDate);
        post2.setUser(user3);
        postRepository.save(post2);

    }
}
