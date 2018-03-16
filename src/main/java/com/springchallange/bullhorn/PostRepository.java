package com.springchallange.bullhorn;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post,Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserOrderByPostDateDesc(User user);
}
