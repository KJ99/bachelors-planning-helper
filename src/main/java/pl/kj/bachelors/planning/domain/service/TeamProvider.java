package pl.kj.bachelors.planning.domain.service;

import pl.kj.bachelors.planning.domain.model.remote.Team;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;

import java.util.Optional;

public interface TeamProvider {
    Optional<Team> get(Integer teamId);
}
