package com.springchallange.bullhorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


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
        user2.setUserImageUrl("/images/UserImage.png");
        user1.setFollowersCount(0);
        user3.setRoles(Arrays.asList(userRole));
        userRepository.save(user3);


    }
}
