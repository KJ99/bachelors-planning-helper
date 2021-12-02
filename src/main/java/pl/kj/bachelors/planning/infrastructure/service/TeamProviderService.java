package pl.kj.bachelors.planning.infrastructure.service;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;
import pl.kj.bachelors.planning.domain.model.remote.Team;
import pl.kj.bachelors.planning.domain.model.remote.UserProfile;
import pl.kj.bachelors.planning.domain.service.TeamProvider;
import pl.kj.bachelors.planning.domain.service.TeamRemoteProvider;
import pl.kj.bachelors.planning.domain.service.user.ProfileProvider;
import pl.kj.bachelors.planning.domain.service.user.ProfileRemoteProvider;
import pl.kj.bachelors.planning.infrastructure.service.cache.CacheManagementService;

import java.util.Optional;

@Service
public class TeamProviderService
        extends BaseCacheableRemoteEntityProvider<Team, TeamRemoteProvider>
        implements TeamProvider {
    @Autowired
    protected TeamProviderService(TeamRemoteProvider remoteEntityProvider, CacheManagementService cacheManagementService) {
        super(remoteEntityProvider, cacheManagementService);
    }

    @Override
    public Optional<Team> get(Integer teamId) {
        Optional<Team> result;
        if(this.noCache || this.shouldSkipCache()) {
            result = this.remoteEntityProvider.get(teamId);
        } else {
            result = this.getFromCache(String.valueOf(teamId)).or(() -> {
                Optional<Team> team = this.remoteEntityProvider.get(teamId);
                team.ifPresent(data -> this.saveToCache(String.valueOf(teamId), data));
                return team;
            });
        }

        return result;
    }

    @Override
    protected CacheTag getCacheTag() {
        return CacheTag.TEAM;
    }

    @Override
    protected Class<Team> getModelClass() {
        return Team.class;
    }
}
