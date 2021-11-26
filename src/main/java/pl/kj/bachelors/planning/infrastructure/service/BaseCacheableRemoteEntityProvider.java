package pl.kj.bachelors.planning.infrastructure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.infrastructure.service.cache.CacheManagementService;
import pl.kj.bachelors.planning.infrastructure.service.remote.BaseRemoteEntityProvider;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.Optional;

public abstract class BaseCacheableRemoteEntityProvider <T, ID, R extends BaseRemoteEntityProvider<T, ID>> {
    protected final R remoteEntityProvider;
    protected final CacheManagementService cacheManagementService;
    protected boolean noCache;

    protected BaseCacheableRemoteEntityProvider(R remoteEntityProvider, CacheManagementService cacheManagementService) {
        this.remoteEntityProvider = remoteEntityProvider;
        this.cacheManagementService = cacheManagementService;
        this.noCache = false;
    }

    protected abstract CacheTag getCacheTag();
    protected abstract Class<T> getModelClass();

    public Optional<T> get(ID entityId) {
        Optional<T> result;
        if(this.noCache || this.shouldSkipCache()) {
            result = this.getFromRemote(entityId);
        } else {
            result = this.getFromCache(entityId).or(() -> this.getFromRemote(entityId));
        }

        return result;
    }

    protected Optional<T> getFromRemote(ID entityId) {
        Optional<T> result = this.remoteEntityProvider.get(entityId);
        result.ifPresent(t -> this.saveToCache(this.getCacheKey(entityId), t));

        return result;
    }

    protected Optional<T> getFromCache(ID entityId) {
        return this.cacheManagementService.read(this.getCacheTag(), this.getCacheKey(entityId), this.getModelClass());
    }

    protected void saveToCache(String key, T value) {
        try {
            this.cacheManagementService.save(this.getCacheTag(), key, value);
        } catch (JsonProcessingException e) {
            String log = String.format("Could not save cache with tag: %s and key: %s", this.getCacheTag(), key);
            LoggerFactory.getLogger(this.getClass()).error(log);
        }
    }

    protected String getCacheKey(ID entityId) {
        return String.valueOf(entityId);
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public boolean isNoCache() {
        return noCache;
    }

    public boolean shouldSkipCache() {
        String cacheControl = RequestHandler.getHeaderValue(HttpHeaders.CACHE_CONTROL).orElse("");
        return cacheControl.contains("no-cache");
    }
}
