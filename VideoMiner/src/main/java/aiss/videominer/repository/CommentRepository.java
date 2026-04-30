package aiss.videominer.repository;

import aiss.videominer.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByVideoId(String videoId);
}
