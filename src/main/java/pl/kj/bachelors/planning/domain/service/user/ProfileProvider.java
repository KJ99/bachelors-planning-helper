package pl.kj.bachelors.planning.domain.service.user;

import pl.kj.bachelors.planning.domain.model.remote.UserProfile;

import java.util.Optional;

public interface ProfileProvider {
    Optional<UserProfile> get(String uid);
}
