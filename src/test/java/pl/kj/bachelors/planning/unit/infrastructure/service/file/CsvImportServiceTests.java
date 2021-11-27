package pl.kj.bachelors.planning.unit.infrastructure.service.file;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.model.create.PlanningItemCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.update.PlanningItemUpdateModel;
import pl.kj.bachelors.planning.infrastructure.service.file.CsvImportService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CsvImportServiceTests extends BaseUnitTest {
    @Autowired
    private CsvImportService service;

    @Test
    public void testImportFromCsv() throws AggregatedApiError {
        String content = "title,description\n" +
                "Task 1,Task 1 desc\n" +
                "Task 2,Task 2 desc";
        var file = new MockMultipartFile("file", content.getBytes(StandardCharsets.UTF_8));

        List<PlanningItemCreateModel> result = this.service.importFromCsv(file, PlanningItemCreateModel.class);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(result.get(0).getDescription()).isEqualTo("Task 1 desc");
        assertThat(result.get(1).getTitle()).isEqualTo("Task 2");
        assertThat(result.get(1).getDescription()).isEqualTo("Task 2 desc");
    }
}
