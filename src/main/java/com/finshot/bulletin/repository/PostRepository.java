package com.finshot.bulletin.repository;


import com.finshot.bulletin.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByIsDeletedFalse();
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}
