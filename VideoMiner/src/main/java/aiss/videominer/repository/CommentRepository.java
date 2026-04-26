package aiss.videominer.repository;

import aiss.videominer.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query(value = "SELECT * FROM comment WHERE video_id = :videoId", nativeQuery = true)
    List<Comment> findByVideoId(@Param("videoId") String videoId);
}
