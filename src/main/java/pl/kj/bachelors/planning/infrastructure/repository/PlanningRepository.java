package pl.kj.bachelors.planning.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.planning.domain.model.entity.Planning;

public interface PlanningRepository extends JpaRepository<Planning, Integer> {
}
