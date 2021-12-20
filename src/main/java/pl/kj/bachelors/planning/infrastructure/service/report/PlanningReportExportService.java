package pl.kj.bachelors.planning.infrastructure.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.report.PlanningItemReport;
import pl.kj.bachelors.planning.domain.model.report.PlanningReport;
import pl.kj.bachelors.planning.domain.service.report.PlanningReportExporter;
import pl.kj.bachelors.planning.domain.service.report.ReportCreator;

import java.util.List;

@Service
public class PlanningReportExportService
        extends BaseReportExportService<Planning, PlanningReport, PlanningItemReport>
        implements PlanningReportExporter {

    @Override
    protected Class<PlanningItemReport> getTableItemModelClass() {
        return PlanningItemReport.class;
    }

    @Override
    protected List<PlanningItemReport> getTableItems(PlanningReport source) {
        return source.getResults();
    }

    @Override
    protected String getDocumentTitle(PlanningReport source) {
        return source.getTitle();
    }

    @Override
    protected String getDocumentSubtitle(PlanningReport source) {
        return String.format("%s %s", source.getTeamName(), source.getDate());
    }
}
