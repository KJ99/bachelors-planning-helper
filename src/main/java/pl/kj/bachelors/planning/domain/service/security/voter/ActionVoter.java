package pl.kj.bachelors.planning.domain.service.security.voter;

import pl.kj.bachelors.planning.domain.model.extension.AccessVote;

public interface ActionVoter<S, A> {
    AccessVote vote(S subject, A action, String userId);
    A[] getSupportedActions();
}
