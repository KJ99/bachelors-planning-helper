package pl.kj.bachelors.planning.domain.service.report;

import com.lowagie.text.Document;

import java.io.OutputStream;

public interface ReportExporter<R> {
    void exportTableReport(R source, OutputStream stream);
}
