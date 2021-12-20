package pl.kj.bachelors.planning.infrastructure.service.security.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.service.security.AccessControlService;
import pl.kj.bachelors.planning.domain.service.security.voter.ActionVoter;
import pl.kj.bachelors.planning.domain.service.security.voter.EntityVoter;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.Set;

@Service
public class AccessControlServiceImpl<S, A> implements AccessControlService<S, A> {
    public final Set<ActionVoter<S, A>> voters;

    @Autowired(required = false)
    public AccessControlServiceImpl(Set<ActionVoter<S, A>> voters) {
        this.voters = voters;
    }

    @Override
    public void ensureThatUserHasAccess(S subject, A action) throws AccessDeniedException {
        String uid = RequestHandler.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        for(ActionVoter<S, A> voter : voters) {
            if(voter.vote(subject, action, uid).equals(AccessVote.DENY)) {
                throw new AccessDeniedException();
            }
        }

    }
}
