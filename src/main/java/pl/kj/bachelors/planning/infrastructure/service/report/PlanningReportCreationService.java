package pl.kj.bachelors.planning.infrastructure.service.report;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.remote.Team;
import pl.kj.bachelors.planning.domain.model.report.PlanningItemReport;
import pl.kj.bachelors.planning.domain.model.report.PlanningReport;
import pl.kj.bachelors.planning.domain.service.TeamProvider;
import pl.kj.bachelors.planning.domain.service.report.PlanningReportCreator;

import java.util.stream.Collectors;

@Service
public class PlanningReportCreationService implements PlanningReportCreator {
    private final TeamProvider teamProvider;
    private final ModelMapper mapper;

    @Autowired
    public PlanningReportCreationService(TeamProvider teamProvider, ModelMapper mapper) {
        this.teamProvider = teamProvider;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public PlanningReport createReport(Planning source) {
        Team team = this.teamProvider.get(source.getTeamId()).orElseThrow();
        PlanningReport report = new PlanningReport();
        report.setTeamName(team.getName());
        this.mapper.map(source, report);
        report.setResults(
                source
                        .getItems()
                        .stream()
                        .map(item -> this.mapper.map(item, PlanningItemReport.class)).collect(Collectors.toList())
        );

        return report;
    }
}
