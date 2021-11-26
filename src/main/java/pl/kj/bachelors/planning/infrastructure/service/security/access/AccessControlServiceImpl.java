package pl.kj.bachelors.planning.infrastructure.service.security.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.domain.service.security.voter.Voter;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.Set;

@Service
public class AccessControlServiceImpl<T> implements EntityAccessControlService<T> {
    protected final Set<Voter<T>> voters;

    @Autowired(required = false)
    public AccessControlServiceImpl(Set<Voter<T>> voters) {
        this.voters = voters;
    }

    @Override
    public void ensureThatUserHasAccess(T subject, Object action) throws AccessDeniedException {
        String uid = RequestHandler.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        for(Voter<T> voter : voters) {
            if(voter.vote(subject, action, uid).equals(AccessVote.DENY)) {
                throw new AccessDeniedException();
            }
        }
    }
}
