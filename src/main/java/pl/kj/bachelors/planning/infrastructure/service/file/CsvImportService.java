package pl.kj.bachelors.planning.infrastructure.service.file;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kj.bachelors.planning.domain.config.ApiConfig;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.service.file.CsvImporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CsvImportService implements CsvImporter {
    private final ApiConfig apiConfig;

    @Autowired
    public CsvImportService(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @Override
    public <T> List<T> importFromCsv(MultipartFile source, Class<T> destinationClass)
            throws AggregatedApiError {
        List<T> result;
        try {
            this.ensureThatFormatIsValid(source);
            result = this.processFile(source, destinationClass);
        } catch (IOException e) {
            var ex = new AggregatedApiError();
            ex.setErrors(List.of(new ApiError(this.apiConfig.getErrors().get("PL.004"), "PL.004", "file")));
            throw ex;
        }

        return result;
    }

    private <T> List<T> processFile(MultipartFile source, Class<T> destinationClass) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(source.getInputStream()));

        CsvToBean<T> bean = new CsvToBeanBuilder<T>(reader)
                .withType(destinationClass)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return bean.parse();
    }

    private void ensureThatFormatIsValid(MultipartFile file) throws IOException, AggregatedApiError {
        Tika tika = new Tika();
        String mediaType = tika.detect(file.getBytes());
        if(!mediaType.equals(MediaType.TEXT_PLAIN_VALUE)) {
            var ex = new AggregatedApiError();
            ex.setErrors(List.of(new ApiError("", "PL.004", "file")));
            throw ex;
        }
    }
}
