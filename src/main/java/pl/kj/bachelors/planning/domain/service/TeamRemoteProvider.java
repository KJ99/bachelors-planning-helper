package pl.kj.bachelors.planning.domain.service;

import pl.kj.bachelors.planning.domain.model.remote.Team;

import java.util.Optional;

public interface TeamRemoteProvider {
    Optional<Team> get(Integer teamId);
}
