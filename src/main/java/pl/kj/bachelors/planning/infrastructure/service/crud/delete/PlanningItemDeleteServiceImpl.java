package pl.kj.bachelors.planning.infrastructure.service.crud.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.service.crud.delete.PlanningItemDeleteService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;

@Service
public class PlanningItemDeleteServiceImpl
        extends BaseEntityDeleteService<PlanningItem, Integer, PlanningItemRepository>
        implements PlanningItemDeleteService {
    @Autowired
    protected PlanningItemDeleteServiceImpl(PlanningItemRepository repository) {
        super(repository);
    }
}
