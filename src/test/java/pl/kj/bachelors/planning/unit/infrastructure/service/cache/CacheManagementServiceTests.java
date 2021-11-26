package pl.kj.bachelors.planning.unit.infrastructure.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.model.entity.CacheItem;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.infrastructure.repository.CacheItemRepository;
import pl.kj.bachelors.planning.infrastructure.service.cache.CacheManagementService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheManagementServiceTests extends BaseUnitTest {
    @Autowired
    private CacheManagementService service;
    @Autowired
    private CacheItemRepository repository;

    public static class Model {
        private String firstName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }

    @Test
    public void testRead_WithoutModelClass() {
        Optional<JsonNode> value = this.service.read(CacheTag.USER_PROFILE, "key-1");

        assertThat(value).isPresent();

        JsonNode node = value.get();
        assertThat(node.has("first_name")).isTrue();
        assertThat(node.get("first_name").textValue()).isNotEmpty().isEqualTo("Francis");
    }

    @Test
    public void testRead_WithModelClass() {
        Optional<Model> result = this.service.read(CacheTag.USER_PROFILE, "key-1", Model.class);

        assertThat(result).isPresent();

        Model value = result.get();
        assertThat(value.firstName).isEqualTo("Francis");
    }

    @Test
    public void testRead_NotFound() {
        Optional<Model> result1 = this.service.read(CacheTag.USER_PROFILE, "fake-tag", Model.class);
        Optional<JsonNode> result2 = this.service.read(CacheTag.USER_PROFILE, "key-2");

        assertThat(result1).isNotPresent();
        assertThat(result2).isNotPresent();
    }

    @Test
    public void testSave() throws JsonProcessingException {
        this.service.save(CacheTag.USER_PROFILE, "runtime-tag-1", "{\"first_name\": \"Bob\"}");
        Optional<CacheItem> candidate = this.repository.findFirstValidByTagAndKey(CacheTag.USER_PROFILE.name(), "runtime-key-1");

        assertThat(candidate.isPresent());
    }

    @Test
    public void testInvalidate() {
        this.service.invalidate();

        List<CacheItem> allItems = this.repository.findAll();
        assertThat(allItems).isEmpty();
    }
}
