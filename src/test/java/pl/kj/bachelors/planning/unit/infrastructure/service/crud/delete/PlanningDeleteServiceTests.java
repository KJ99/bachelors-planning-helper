package pl.kj.bachelors.planning.unit.infrastructure.service.crud.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.crud.delete.PlanningDeleteServiceImpl;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PlanningDeleteServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningDeleteServiceImpl service;

    @Autowired
    private PlanningRepository repository;

    @Test
    public void testDelete() {
        Planning planning = this.repository.findById(1).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.delete(planning));
        assertThat(thrown).isNull();
        assertThat(this.repository.findById(1)).isNotPresent();
    }

    @Test
    public void testDelete_AccessDenied() {
        Planning planning = this.repository.findById(2).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.delete(planning));
        assertThat(thrown).isInstanceOf(AccessDeniedException.class);
        assertThat(this.repository.findById(2)).isPresent();
    }
}
