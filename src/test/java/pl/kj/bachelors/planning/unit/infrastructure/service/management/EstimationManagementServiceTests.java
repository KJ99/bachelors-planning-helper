package pl.kj.bachelors.planning.unit.infrastructure.service.management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.management.EstimationManagementService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class EstimationManagementServiceTests extends BaseUnitTest {
    @Autowired
    private EstimationManagementService service;
    @Autowired
    private PlanningItemRepository repository;
    @Autowired
    private PlanningRepository planningRepository;

    @Test
    public void testSetEstimation() {
        Planning planning = this.planningRepository.findById(4).orElseThrow();
        PlanningItem item = this.repository.findFirstByPlanningAndFocused(planning, true).orElseThrow();

        Throwable thrown = catchThrowable(() -> this.service.setEstimation(item, 8));
        assertThat(thrown).isNull();
        assertThat(item.getEstimation()).isEqualTo(8);
    }

    @Test
    public void testSetEstimation_NotFocused() {
        Planning planning = this.planningRepository.findById(4).orElseThrow();
        PlanningItem item = this.repository.findFirstByPlanningAndFocused(planning, false).orElseThrow();

        Throwable thrown = catchThrowable(() -> this.service.setEstimation(item, 8));
        assertThat(thrown).isInstanceOf(ApiError.class);
    }
}
