package pl.kj.bachelors.planning.domain.service.crud.read;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.search.PlanningSearchModel;

import java.util.Optional;

public interface PlanningReadService {
    Page<Planning> readPagedByTeam(Integer teamId, Pageable pageQuery, PlanningSearchModel searchQuery);
    Optional<Planning> readParticular(Integer planningId);
    Optional<Planning> readIncoming(Integer teamId);
}
