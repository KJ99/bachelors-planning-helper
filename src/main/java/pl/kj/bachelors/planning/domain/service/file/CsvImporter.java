package pl.kj.bachelors.planning.domain.service.file;

import org.springframework.web.multipart.MultipartFile;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;

import java.io.IOException;
import java.util.List;

public interface CsvImporter {
    <T> List<T> importFromCsv(MultipartFile source, Class<T> destinationClass) throws AggregatedApiError;
}
