package pl.kj.bachelors.planning.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;

import java.util.List;
import java.util.Optional;

public interface PlanningItemRepository extends JpaRepository<PlanningItem, Integer> {
    List<PlanningItem> findByPlanning(Planning planning);
    Optional<PlanningItem> findFirstByPlanningAndFocused(Planning planning, boolean focused);
}
