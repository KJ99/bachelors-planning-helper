package pl.kj.bachelors.planning.domain.service.security;

import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;

public interface AccessControlService<S, A> {
    void ensureThatUserHasAccess(S subject, A action) throws AccessDeniedException;
}
