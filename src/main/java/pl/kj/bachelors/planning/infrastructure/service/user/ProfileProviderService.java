package pl.kj.bachelors.planning.infrastructure.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.domain.model.remote.UserProfile;
import pl.kj.bachelors.planning.domain.service.user.ProfileProvider;
import pl.kj.bachelors.planning.domain.service.user.ProfileRemoteProvider;
import pl.kj.bachelors.planning.infrastructure.service.BaseCacheableRemoteEntityProvider;
import pl.kj.bachelors.planning.infrastructure.service.cache.CacheManagementService;
import pl.kj.bachelors.planning.infrastructure.service.remote.ProfileRemoteProviderImpl;

import java.util.Optional;

@Service
public class ProfileProviderService
        extends BaseCacheableRemoteEntityProvider<UserProfile, ProfileRemoteProvider>
        implements ProfileProvider {
    @Autowired
    public ProfileProviderService(
            ProfileRemoteProviderImpl remoteEntityProvider,
            CacheManagementService cacheManagementService
    ) {
        super(remoteEntityProvider, cacheManagementService);
    }

    @Override
    public Optional<UserProfile> get(String uid) {
        Optional<UserProfile> result;
        if(this.noCache || this.shouldSkipCache()) {
            result = this.remoteEntityProvider.get(uid);
        } else {
            result = this.getFromCache(uid).or(() -> {
                Optional<UserProfile> profile = this.remoteEntityProvider.get(uid);
                profile.ifPresent(data -> this.saveToCache(uid, data));
                return profile;
            });
        }

        return result;
    }

    @Override
    protected CacheTag getCacheTag() {
        return CacheTag.USER_PROFILE;
    }

    @Override
    protected Class<UserProfile> getModelClass() {
        return UserProfile.class;
    }
}
