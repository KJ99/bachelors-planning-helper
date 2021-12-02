package pl.kj.bachelors.planning.unit.infrastructure.service.report;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;
import pl.kj.bachelors.planning.domain.model.remote.Team;
import pl.kj.bachelors.planning.domain.model.report.PlanningItemReport;
import pl.kj.bachelors.planning.domain.model.report.PlanningReport;
import pl.kj.bachelors.planning.domain.service.TeamRemoteProvider;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.report.PlanningReportExportService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.io.BufferedOutputStream;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class PlanningReportExportServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningReportExportService service;
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
    public void testExportTableReport() {
        PlanningReport report = new PlanningReport();
        PlanningItemReport item1 = new PlanningItemReport();
        item1.setTitle("item 1");
        item1.setEstimation(Estimation.M.name());
        item1.setStoryPoints(2);
        PlanningItemReport item2 = new PlanningItemReport();
        item2.setTitle("item 2");
        item2.setEstimation(Estimation.M.name());
        item2.setStoryPoints(2);

        report.setTitle("Report 1");
        report.setDate("2020-01-01");
        report.setTeamName("Team 1");
        report.setResults(Arrays.asList(item1, item2));
        MockHttpServletResponse response = new MockHttpServletResponse();

        this.service.exportTableReport(report, response.getOutputStream());
    }
}
