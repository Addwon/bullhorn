package com.springchallange.bullhorn;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);

    Long countByEmail(String email);
    Long countByUsername(String username);
    User findDistinctByRoles(String role);
    Collection<User>findByFollowing(User user);

}
