package pl.kj.bachelors.planning.unit.infrastructure.service.management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.management.PlanningManagementService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PlanningManagementServiceTests extends BaseUnitTest {
    @Autowired
    PlanningRepository repository;
    @Autowired
    PlanningManagementService service;

    @Test
    public void testOpen() {
        Planning planning = this.repository.findById(1).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.open(planning));
        assertThat(thrown).isNull();
        assertThat(planning.getStatus()).isEqualTo(PlanningStatus.PROGRESSING);
    }

    @Test
    public void testOpen_WrongStatus() {
        Planning planning = this.repository.findById(2).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.open(planning));
        assertThat(thrown).isInstanceOf(ApiError.class);
        ApiError ex = (ApiError) thrown;
        assertThat(ex.getCode()).isEqualTo("PL.101");
    }

    @Test
    public void testClose() {
        Planning planning = this.repository.findById(4).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.close(planning));
        assertThat(thrown).isNull();
        assertThat(planning.getStatus()).isEqualTo(PlanningStatus.FINISHED);
    }

    @Test
    public void testClose_WrongStatus() {
        Planning planning = this.repository.findById(2).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.close(planning));
        assertThat(thrown).isInstanceOf(ApiError.class);
        ApiError ex = (ApiError) thrown;
        assertThat(ex.getCode()).isEqualTo("PL.102");
    }

    @Test
    public void testEnableVoting() {
        Planning planning = this.repository.findById(4).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.enableVoting(planning));
        assertThat(thrown).isNull();
        assertThat(planning.getStatus()).isEqualTo(PlanningStatus.VOTING);
    }

    @Test
    public void testEnableVoting_WrongStatus() {
        Planning planning = this.repository.findById(1).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.enableVoting(planning));
        assertThat(thrown).isInstanceOf(ApiError.class);
    }


    @Test
    public void testDisableVoting_WrongStatus() {
        Planning planning = this.repository.findById(4).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.disableVoting(planning));
        assertThat(thrown).isInstanceOf(ApiError.class);
    }
}
