package com.dalhousie.Neighbourly.post.repository;

import com.dalhousie.Neighbourly.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByNeighbourhoodIdAndApproved(int neighbourhoodId, boolean approved);
    List<Post> findByNeighbourhoodId(int neighbourhoodId);
}   