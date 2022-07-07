package com.javlasov.blog.repository;

import com.javlasov.blog.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {

    Optional<List<PostVotes>> findByUserId(int userId);

    @Query(value = "FROM PostVotes where postId = ?1 AND userId= ?2")
    Optional<PostVotes> findByUserIdAndPostId(int postId, int userId);

}
