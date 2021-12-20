package pl.kj.bachelors.planning.unit.infrastructure.service.crud.delete;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.crud.delete.PlanningDeleteServiceImpl;
import pl.kj.bachelors.planning.infrastructure.service.crud.delete.PlanningItemDeleteServiceImpl;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PlanningItemDeleteServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningItemDeleteServiceImpl service;

    @Autowired
    private PlanningItemRepository repository;

    @Test
    public void testDelete() {
        PlanningItem item = this.repository.findById(1).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.delete(item));
        assertThat(thrown).isNull();
        assertThat(this.repository.findById(1)).isNotPresent();
    }
}
