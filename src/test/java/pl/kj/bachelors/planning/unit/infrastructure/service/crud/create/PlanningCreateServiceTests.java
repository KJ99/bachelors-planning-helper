package pl.kj.bachelors.planning.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.infrastructure.service.crud.create.PlanningCreateServiceImpl;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

public class PlanningCreateServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningCreateServiceImpl service;
    @BeforeEach
    public void setUp() {
        when(RequestHandler.getRequestTimeZone()).thenReturn(Optional.of(TimeZone.getDefault()));
    }

    @Test
    public void testCreate() throws Exception {
        PlanningCreateModel model = new PlanningCreateModel();
        model.setTitle("Planning 123");
        model.setTeamId(1);
        model.setStartDate("2030-12-20 23:59:59");
        Planning planning = this.service.create(model, Planning.class);
        assertThat(planning).isNotNull();
        assertThat(planning.getId()).isPositive();
        assertThat(planning.getTitle()).isEqualTo("Planning 123");
        assertThat(planning.getStatus()).isEqualTo(PlanningStatus.SCHEDULED);
        assertThat(planning.getStartAt()).isGreaterThan(Calendar.getInstance());
        assertThat(planning.getTimeZone()).isEqualTo(TimeZone.getDefault().getID());
    }

    @Test
    public void testCreate_ValidationFails() {
        PlanningCreateModel model = new PlanningCreateModel();
        model.setTitle("  ");
        model.setTeamId(0);
        model.setStartDate("2000-12-20 23:59:59");
        Throwable thrown = catchThrowable(() -> this.service.create(model, Planning.class));
        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        AggregatedApiError ex = (AggregatedApiError) thrown;
        assertThat(ex.getErrors()).hasSize(3);
    }
}
