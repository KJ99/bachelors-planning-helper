package pl.kj.bachelors.planning.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;

import java.util.Optional;

public interface PlanningRepository extends JpaRepository<Planning, Integer> {
    Page<Planning> findByTeamIdAndStatus(Integer teamId, PlanningStatus status, Pageable pageable);
    Page<Planning> findByTeamId(Integer teamId, Pageable pageable);

    @Query("select p from Planning p where p.status != 'FINISHED' order by p.startAt asc")
    Optional<Planning> findFirstNotFinished(Integer teamId);
}
