package pl.kj.bachelors.planning.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.entity.Vote;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByPlanningItem(PlanningItem item);
    void deleteByPlanningItem(PlanningItem item);
}
