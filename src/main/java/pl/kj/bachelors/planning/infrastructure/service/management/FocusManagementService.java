package pl.kj.bachelors.planning.infrastructure.service.management;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.event.FocusChangedEvent;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.service.management.FocusManager;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FocusManagementService extends BaseManagementService implements FocusManager {
    private final PlanningItemRepository repository;

    @Autowired
    public FocusManagementService(PlanningItemRepository repository, ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
        this.repository = repository;
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void focus(PlanningItem item) throws ApiError {
        this.ensureThatStatusIsCorrect(item.getPlanning());
        this.clearFocus(item.getPlanning());
        item.setFocused(true);
        this.repository.save(item);
        this.publishEvent(new FocusChangedEvent(
                this,
                item.getPlanning().getId(),
                item.getId()
        ));
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void focusNext(Planning planning) throws ApiError {
        this.ensureThatStatusIsCorrect(planning);
        List<PlanningItem> items = this.repository.findByPlanningOrderByAuditCreatedAtAsc(planning);
        int index = this.findCurrentFocusIndex(items) + 1;
        this.clearFocus(planning);
        Integer focusedItemId = null;
        if(index < items.size()) {
            PlanningItem item = items.get(index);
            item.setFocused(true);
            this.repository.save(item);
            focusedItemId = item.getId();
        }

        this.publishEvent(new FocusChangedEvent(
                this,
                planning.getId(),
                focusedItemId
        ));
    }

    @Override
    @Transactional
    public boolean hasNext(Planning planning) {
        List<PlanningItem> items = this.repository.findByPlanningOrderByAuditCreatedAtAsc(planning);
        int index = this.findCurrentFocusIndex(items);

        return index < items.size() - 1;
    }

    private int findCurrentFocusIndex(List<PlanningItem> items) {
        int index = -1;

        for (int i = 0; i < items.size() && index < 0; i++) {
            if(items.get(i).isFocused()) {
                index = i;
            }
        }

        return index;
    }

    private void clearFocus(Planning planning) {
        for(PlanningItem item : planning.getItems()) {
            item.setFocused(false);
            this.repository.save(item);
        }
    }

    private void ensureThatStatusIsCorrect(Planning planning) throws ApiError {
        if(!planning.hasStatusIn(Arrays.asList(PlanningStatus.PROGRESSING, PlanningStatus.VOTING))) {
            throw new ApiError("", "PL.111", null);
        }
    }
}
