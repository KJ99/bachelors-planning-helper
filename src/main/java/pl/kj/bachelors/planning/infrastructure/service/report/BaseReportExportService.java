package pl.kj.bachelors.planning.infrastructure.service.report;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.annotation.PdfColumn;
import pl.kj.bachelors.planning.domain.model.config.PdfColumnConfig;
import pl.kj.bachelors.planning.domain.service.report.ReportCreator;
import pl.kj.bachelors.planning.domain.service.report.ReportExporter;

import java.awt.*;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseReportExportService<S, R, I> implements ReportExporter<R> {

    @Override
    @Transactional
    public void exportTableReport(R source, OutputStream outputStream) {
        Document document = new Document(this.getPageSize());
        PdfWriter.getInstance(document, outputStream);
        document.open();
        this.writeDocumentHead(document, source);
        this.writeTable(document, source);
        document.close();

    }

    protected void writeDocumentHead(Document document, R source) {
        Paragraph title = new Paragraph(this.getDocumentTitle(source), this.getBoldFont());
        Paragraph subtitle = new Paragraph(getDocumentSubtitle(source), this.getItalicFont());

        document.add(title);
        document.add(subtitle);
    }

    protected void writeTable(Document document, R source) {
        List<PdfColumnConfig> columns = this.getColumns();
        PdfPTable table = this.initializeTable(columns);
        this.writeHeader(table, columns);
        this.writeTableBody(this.getTableItems(source), table, columns);

        document.add(table);
    }

    protected void writeHeader(PdfPTable table, List<PdfColumnConfig> columns) {
        for(var column : columns) {
            PdfPCell cell = new PdfPCell();
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.getHSBColor(202, 40, 50));
            Font font = this.getBoldFont();
            cell.setPhrase(new Phrase(column.getDisplayName(), font));
            table.addCell(cell);
        }
    }

    protected void writeTableBody(List<I> data, PdfPTable table, List<PdfColumnConfig> columns) {
        Class<I> modelClass = this.getTableItemModelClass();
        Font font = this.getFont();
        for(I item : data) {
            for(PdfColumnConfig column : columns) {
                Object value;
                try {
                    Field field = modelClass.getDeclaredField(column.getFieldName());
                    field.setAccessible(true);
                    value = field.get(item);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    value = "";
                }

                table.addCell(new Phrase(value.toString(), font));
            }
        }
    }

    protected PdfPTable initializeTable(List<PdfColumnConfig> columns) {
        PdfPTable table = new PdfPTable(columns.size());
        table.setWidthPercentage(75f);
        Float[] widths = columns.stream().map(PdfColumnConfig::getRelativeWidth).toArray(Float[]::new);
        table.setWidths(ArrayUtils.toPrimitive(widths));
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(30f);
        table.setSpacingAfter(5f);

        return table;
    }

    protected List<PdfColumnConfig> getColumns() {
        Class<I> modelClass = this.getTableItemModelClass();
        Field[] fields = modelClass.getDeclaredFields();
        List<PdfColumnConfig> columns = new ArrayList<>();

        for(var field : fields) {
            PdfColumn column = field.getAnnotation(PdfColumn.class);
            if(column != null) {
                String displayName = column.name().length() > 0 ? column.name() : field.getName();
                columns.add(new PdfColumnConfig(
                        field.getName(),
                        displayName,
                        column.relativeWidth(),
                        column.order()
                ));
            }
        }

        columns.sort(Comparator.comparingInt(PdfColumnConfig::getOrder));

        return columns;
    }

    protected Rectangle getPageSize() {
        return PageSize.A4;
    }

    protected Font getFont() {
        return FontFactory.getFont(FontFactory.TIMES_ROMAN);
    }

    protected Font getBoldFont() {
        return FontFactory.getFont(FontFactory.TIMES_BOLD);
    }

    protected Font getItalicFont() {
        return FontFactory.getFont(FontFactory.TIMES_ITALIC);
    }

    protected abstract Class<I> getTableItemModelClass();

    protected abstract List<I> getTableItems(R source);

    protected abstract String getDocumentTitle(R source);

    protected abstract String getDocumentSubtitle(R source);
}
