package pl.kj.bachelors.planning.infrastructure.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.model.entity.CacheItem;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.domain.service.cache.CacheManager;
import pl.kj.bachelors.planning.infrastructure.config.CacheConfig;
import pl.kj.bachelors.planning.infrastructure.repository.CacheItemRepository;

import java.util.Calendar;
import java.util.Optional;

@Service
public class CacheManagementService implements CacheManager {
    private final CacheItemRepository repository;
    private final ObjectMapper objectMapper;
    private final CacheConfig config;

    @Autowired
    public CacheManagementService(CacheItemRepository repository, ObjectMapper objectMapper, CacheConfig config) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Optional<JsonNode> read(CacheTag tag, @NonNull String key) {
        return this.read(tag, key, JsonNode.class);
    }

    @Override
    public <T> Optional<T> read(CacheTag tag, @NonNull String key, Class<T> modelClass) {
        Optional<CacheItem> cacheItem = this.repository.findFirstValidByTagAndKey(tag.name(), key);
        return cacheItem.map(item -> this.mapValue(item, modelClass));
    }

    @Override
    @Transactional
    public void save(CacheTag tag, @NonNull String key, Object value) throws JsonProcessingException {
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.HOUR, config.getValidTimeInHours());

        CacheItem cacheItem = new CacheItem();
        cacheItem.setTag(tag);
        cacheItem.setKey(key);
        cacheItem.setExpiresAt(expiresAt);
        cacheItem.setValue(this.objectMapper.writeValueAsString(value));

        this.repository.save(cacheItem);
    }

    @Override
    @Transactional
    public void invalidate() {
        this.repository.deleteAll();
    }

    private <T> T mapValue(CacheItem item, Class<T> modelClass) {
        T result;
        try {
            result = this.objectMapper.readValue(item.getValue(), modelClass);
        } catch (JsonProcessingException e) {
            result = null;
        }
        return result;

    }
}
