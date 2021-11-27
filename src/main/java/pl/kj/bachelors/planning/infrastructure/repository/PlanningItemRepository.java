package pl.kj.bachelors.planning.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;

public interface PlanningItemRepository extends JpaRepository<PlanningItem, Integer> {
}
