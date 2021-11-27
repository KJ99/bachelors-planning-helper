package pl.kj.bachelors.planning.infrastructure.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.model.remote.UserProfile;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;
import pl.kj.bachelors.planning.domain.service.user.MemberRemoteProvider;
import pl.kj.bachelors.planning.infrastructure.service.BaseCacheableRemoteEntityProvider;
import pl.kj.bachelors.planning.infrastructure.service.cache.CacheManagementService;

import java.util.Optional;

@Service
public class MemberProviderService
        extends BaseCacheableRemoteEntityProvider<TeamMember, MemberRemoteProvider>
        implements MemberProvider {
    @Autowired
    protected MemberProviderService(MemberRemoteProvider remoteEntityProvider, CacheManagementService cacheManagementService) {
        super(remoteEntityProvider, cacheManagementService);
    }

    private String createCacheKey(Integer teamId, String userId) {
        return String.format("%s:%s", teamId, userId);
    }

    @Override
    public Optional<TeamMember> get(Integer teamId, String uid) {
        String cacheKey = this.createCacheKey(teamId, uid);
        Optional<TeamMember> result;
        if(this.noCache || this.shouldSkipCache()) {
            result = this.remoteEntityProvider.get(teamId, uid);
        } else {
            result = this.getFromCache(cacheKey).or(() -> {
                Optional<TeamMember> member = this.remoteEntityProvider.get(teamId, uid);
                member.ifPresent(data -> this.saveToCache(cacheKey, data));
                return member;
            });
        }

        return result;
    }

    @Override
    protected CacheTag getCacheTag() {
        return CacheTag.TEAM_MEMBER;
    }

    @Override
    protected Class<TeamMember> getModelClass() {
        return TeamMember.class;
    }
}
