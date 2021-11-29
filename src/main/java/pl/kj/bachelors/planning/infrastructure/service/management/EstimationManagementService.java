package pl.kj.bachelors.planning.infrastructure.service.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.event.ItemEstimatedEvent;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;
import pl.kj.bachelors.planning.domain.service.management.EstimationManager;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;

@Service
public class EstimationManagementService extends BaseManagementService implements EstimationManager {
    private final PlanningItemRepository repository;

    @Autowired
    public EstimationManagementService(PlanningItemRepository repository, ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
        this.repository = repository;
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void setEstimation(PlanningItem item, Estimation estimation) throws ApiError {
        if(!item.isFocused()) {
            throw new ApiError("", "PL.122", null);
        }

        item.setEstimation(estimation);

        this.repository.save(item);

        this.publishEvent(new ItemEstimatedEvent(
                this,
                item.getPlanning().getId(),
                item.getId(),
                estimation
        ));
    }
}
