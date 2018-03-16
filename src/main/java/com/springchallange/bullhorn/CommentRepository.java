package com.springchallange.bullhorn;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findByPostOrderByCommentDateDesc(Post post);
}
