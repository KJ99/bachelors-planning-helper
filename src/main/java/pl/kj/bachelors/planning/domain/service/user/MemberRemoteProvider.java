package pl.kj.bachelors.planning.domain.service.user;

import pl.kj.bachelors.planning.domain.model.remote.TeamMember;

import java.util.Optional;

public interface MemberRemoteProvider {
    Optional<TeamMember> get(Integer teamId, String uid);
}
