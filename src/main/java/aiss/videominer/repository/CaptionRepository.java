package aiss.videominer.repository;

import aiss.videominer.model.Caption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaptionRepository extends JpaRepository<Caption, String> {
    @Query(value = "SELECT * FROM caption WHERE video_id = :videoId", nativeQuery = true)
    List<Caption> findByVideoId(@Param("videoId") String videoId);
}
