package pl.kj.bachelors.planning.infrastructure.service.crud.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.service.crud.delete.PlanningDeleteService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;

@Service
public class PlanningDeleteServiceImpl
        extends BaseEntityDeleteService<Planning, Integer, PlanningRepository>
        implements PlanningDeleteService {
    @Autowired
    protected PlanningDeleteServiceImpl(PlanningRepository repository) {
        super(repository);
    }

    @Override
    protected void preDelete(Planning entity) throws Exception {
        if(!entity.getStatus().equals(PlanningStatus.SCHEDULED)) {
            throw new AccessDeniedException("Cannot delete finished or progressing planning!");
        }
    }
}
