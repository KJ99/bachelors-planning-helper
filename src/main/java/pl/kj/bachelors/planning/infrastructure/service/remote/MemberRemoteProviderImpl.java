package pl.kj.bachelors.planning.infrastructure.service.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.config.JwtConfig;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;
import pl.kj.bachelors.planning.domain.service.user.MemberRemoteProvider;
import pl.kj.bachelors.planning.infrastructure.config.TeamsApiConfig;

import java.util.Optional;

@Service
public class MemberRemoteProviderImpl extends BaseRemoteEntityProvider<TeamMember> implements MemberRemoteProvider {
    private final TeamsApiConfig config;
    @Autowired
    public MemberRemoteProviderImpl(JwtConfig jwtConfig, ObjectMapper objectMapper, TeamsApiConfig config) {
        super(jwtConfig, objectMapper);
        this.config = config;
    }

    @Override
    public Optional<TeamMember> get(Integer teamId, String uid) {
        String url = String.format("%s/v1/teams/%s/roles/%s", this.config.getHost(), teamId, uid);
        return this.fetch(url);
    }

    @Override
    protected Class<TeamMember> getModelClass() {
        return TeamMember.class;
    }
}
