package pl.kj.bachelors.planning.unit.infrastructure.service.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;
import pl.kj.bachelors.planning.domain.model.remote.Team;
import pl.kj.bachelors.planning.domain.model.report.PlanningReport;
import pl.kj.bachelors.planning.domain.service.TeamRemoteProvider;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.report.PlanningReportCreationService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class PlanningReportCreationServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningReportCreationService service;
    @Autowired
    private PlanningRepository planningRepository;
    @MockBean
    private TeamRemoteProvider remoteProvider;

    @BeforeEach
    public void setUp() {
        Team team = new Team();
        team.setId(100);
        team.setName("First remote team");

        given(this.remoteProvider.get(120)).willReturn(Optional.of(team));
    }

    @Test
    @Transactional
    public void testCreateReport() {
        Planning planning = this.planningRepository.findById(100).orElseThrow();

        PlanningReport report = this.service.createReport(planning);

        assertThat(report).isNotNull();
        assertThat(report.getTitle()).isEqualTo(planning.getTitle());
        assertThat(report.getDate()).isEqualTo("Saturday, January 1, 2011");
        assertThat(report.getTeamName()).isEqualTo("First remote team");
        assertThat(report.getResults()).allMatch(res -> res.getStoryPoints() >= 0);
        assertThat(report.getResults()).allMatch(res -> res.getEstimation() != null);
        assertThat(report.getResults()).allMatch(res -> res.getTitle() != null);
    }
}
