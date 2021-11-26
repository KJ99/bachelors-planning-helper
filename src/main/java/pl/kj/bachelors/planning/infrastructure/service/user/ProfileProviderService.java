package pl.kj.bachelors.planning.infrastructure.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.domain.model.remote.UserProfile;
import pl.kj.bachelors.planning.domain.service.user.ProfileProvider;
import pl.kj.bachelors.planning.infrastructure.service.BaseCacheableRemoteEntityProvider;
import pl.kj.bachelors.planning.infrastructure.service.cache.CacheManagementService;
import pl.kj.bachelors.planning.infrastructure.service.remote.ProfileRemoteProvider;

@Service
public class ProfileProviderService
        extends BaseCacheableRemoteEntityProvider<UserProfile, String, ProfileRemoteProvider>
        implements ProfileProvider {
    @Autowired
    public ProfileProviderService(
            ProfileRemoteProvider remoteEntityProvider,
            CacheManagementService cacheManagementService
    ) {
        super(remoteEntityProvider, cacheManagementService);
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
