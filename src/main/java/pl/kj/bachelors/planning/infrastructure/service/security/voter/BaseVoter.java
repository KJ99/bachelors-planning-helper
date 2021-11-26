package pl.kj.bachelors.planning.infrastructure.service.security.voter;

import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.service.security.voter.Voter;

import java.util.Arrays;

public abstract class BaseVoter <T, A> implements Voter<T> {
    @Override
    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public AccessVote vote(T subject, Object action, String userId) {
        return Arrays.asList(this.getSupportedActions()).contains(action)
                ? voteInternal(subject, (A)action, userId)
                : AccessVote.OMIT;
    }

    protected abstract AccessVote voteInternal(T subject, A action, String userId);

    protected abstract A[] getSupportedActions();
}
