package aiss.videominer.repository;

import aiss.videominer.model.Caption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaptionRepository extends JpaRepository<Caption, String> {
    List<Caption> findByVideoId(String videoId);
}
