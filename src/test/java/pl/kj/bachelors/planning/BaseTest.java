package pl.kj.bachelors.planning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import pl.kj.bachelors.planning.application.Application;
import pl.kj.bachelors.planning.application.config.*;

@SpringBootTest
@ContextConfiguration(classes = {
        Application.class,
        DbConfig.class,
        MapperConfig.class,
        ModelConfig.class,
        ServerConfig.class,
        StorageConfig.class
})
@Sql(value = "/db.test/seed.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db.test/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BaseTest {
    @Autowired
    protected ObjectMapper objectMapper;

    protected String serialize(Object model) {
        String json;
        try {
            json = this.objectMapper.writer().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            json = "";
        }

        return json;
    }

    protected <T> T deserialize(String value, Class<T> destinationClass) {
        T result;
        try {
            result = this.objectMapper.readValue(value, destinationClass);
        } catch (JsonProcessingException e) {
            result = null;
        }

        return result;
    }

    protected String getCurrentUserId() {
        return "uid-1";
    }
}
