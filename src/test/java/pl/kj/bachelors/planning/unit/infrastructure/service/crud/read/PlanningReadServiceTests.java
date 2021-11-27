package pl.kj.bachelors.planning.unit.infrastructure.service.crud.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.search.PlanningSearchModel;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.crud.read.PlanningReadServiceImpl;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PlanningReadServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningReadServiceImpl service;

    @BeforeEach
    public void setUp() {
        when(RequestHandler.getRequestTimeZone()).thenReturn(Optional.of(TimeZone.getTimeZone("Europe/Amsterdam")));
    }

    @Test
    public void testReadPagedByTeam_WithoutStatusFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        PlanningSearchModel searchModel = new PlanningSearchModel();

        Page<Planning> result = this.service.readPagedByTeam(1, pageable, searchModel);

        assertThat(result.getContent()).isNotEmpty();
    }

    @Test
    public void testReadPagedByTeam_WithStatusFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        PlanningSearchModel searchModel = new PlanningSearchModel();
        searchModel.setStatus(PlanningStatus.SCHEDULED);

        Page<Planning> result = this.service.readPagedByTeam(1, pageable, searchModel);

        assertThat(result.getContent())
                .isNotEmpty()
                .allMatch(planning -> planning.getStatus().equals(PlanningStatus.SCHEDULED))
                .allMatch(planning ->
                        planning.getStartAt().getTimeZone().equals(TimeZone.getTimeZone("Europe/Amsterdam"))
                );
    }

    @Test
    public void testReadParticular() {
        Optional<Planning> result = this.service.readParticular(1);

        assertThat(result).isPresent();
        assertThat(result.get().getStartAt().getTimeZone().equals(TimeZone.getTimeZone("Europe/Amsterdam")));
    }

    @Test
    public void testReadIncoming() {
        Optional<Planning> result = this.service.readIncoming(1);

        assertThat(result).isPresent();
        assertThat(result.get().getStartAt().getTimeZone().equals(TimeZone.getTimeZone("Europe/Amsterdam")));
        assertThat(result.get().getId()).isEqualTo(3);
    }
}
