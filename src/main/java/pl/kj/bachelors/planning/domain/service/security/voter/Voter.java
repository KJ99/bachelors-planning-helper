package pl.kj.bachelors.planning.domain.service.security.voter;

import pl.kj.bachelors.planning.domain.model.extension.AccessVote;

public interface Voter<T> {
    AccessVote vote(T subject, Object action, String userId);
}
