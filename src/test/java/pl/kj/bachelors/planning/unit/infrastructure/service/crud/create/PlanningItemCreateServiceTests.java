package pl.kj.bachelors.planning.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.model.create.PlanningItemCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.crud.create.PlanningItemCreateServiceImpl;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PlanningItemCreateServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningItemCreateServiceImpl service;
    @Autowired
    private PlanningRepository planningRepository;

    @Test
    public void testCreate() throws Exception {
        Planning planning = this.planningRepository.findById(1).orElseThrow();
        var model = new PlanningItemCreateModel();
        model.setTitle("Some task");
        model.setDescription("Description");
        model.setPlanning(planning);

        PlanningItem item = this.service.create(model, PlanningItem.class);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isPositive();
        assertThat(item.getPlanning()).isNotNull();
        assertThat(item.getPlanning().getId()).isEqualTo(1);
        assertThat(item.getTitle()).isEqualTo("Some task");
        assertThat(item.getDescription()).isEqualTo("Description");
    }

    @Test
    public void testCreate_ValidationFails() {
        Planning planning = this.planningRepository.findById(1).orElseThrow();
        var model = new PlanningItemCreateModel();
        model.setTitle("  ");
        model.setDescription("Description");
        model.setPlanning(planning);

        Throwable thrown = catchThrowable(() -> this.service.create(model, PlanningItem.class));

        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        AggregatedApiError ex = (AggregatedApiError) thrown;
        assertThat(ex.getErrors()).isNotEmpty().hasSize(1);
    }
}
