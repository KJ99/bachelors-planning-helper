package pl.kj.bachelors.planning.domain.service.report;

public interface ReportCreator<T, R> {
    R createReport(T source);
}
