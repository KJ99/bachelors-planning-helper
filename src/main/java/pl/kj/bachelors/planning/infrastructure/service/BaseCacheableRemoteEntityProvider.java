package pl.kj.bachelors.planning.infrastructure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.infrastructure.service.cache.CacheManagementService;
import pl.kj.bachelors.planning.infrastructure.service.remote.BaseRemoteEntityProvider;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.Optional;

public abstract class BaseCacheableRemoteEntityProvider <T, R> {
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


    protected Optional<T> getFromCache(String key) {
        return this.cacheManagementService.read(this.getCacheTag(), key, this.getModelClass());
    }

    protected void saveToCache(String key, T value) {
        try {
            this.cacheManagementService.save(this.getCacheTag(), key, value);
        } catch (JsonProcessingException e) {
            String log = String.format("Could not save cache with tag: %s and key: %s", this.getCacheTag(), key);
            LoggerFactory.getLogger(this.getClass()).error(log);
        }
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
