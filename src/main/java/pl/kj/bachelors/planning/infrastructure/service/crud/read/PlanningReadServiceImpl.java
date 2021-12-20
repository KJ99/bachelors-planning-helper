package pl.kj.bachelors.planning.infrastructure.service.crud.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.search.PlanningSearchModel;
import pl.kj.bachelors.planning.domain.service.crud.read.PlanningReadService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.Optional;
import java.util.TimeZone;

@Service
public class PlanningReadServiceImpl implements PlanningReadService {
    private final PlanningRepository repository;

    @Autowired
    public PlanningReadServiceImpl(PlanningRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Planning> readPagedByTeam(Integer teamId, Pageable pageQuery, PlanningSearchModel searchQuery) {
        Page<Planning> page = searchQuery.getStatus() != null
                ? this.repository.findByTeamIdAndStatus(teamId, searchQuery.getStatus(), pageQuery)
                : this.repository.findByTeamId(teamId, pageQuery);

        return page.map(this::applyTimeZone);
    }

    @Override
    public Optional<Planning> readParticular(Integer planningId) {
        Optional<Planning> planning = this.repository.findById(planningId);
        return planning.map(this::applyTimeZone);
    }

    @Override
    public Optional<Planning> readIncoming(Integer teamId) {
        Optional<Planning> planning = this.repository
                .findFirstByTeamIdAndStatusOrderByStartAtAsc(teamId, PlanningStatus.SCHEDULED);
        return planning.map(this::applyTimeZone);
    }

    public Planning applyTimeZone(Planning planning) {
        TimeZone tz = RequestHandler.getRequestTimeZone().orElse(TimeZone.getTimeZone(planning.getTimeZone()));
        planning.getStartAt().setTimeZone(tz);
        return planning;
    }
}
