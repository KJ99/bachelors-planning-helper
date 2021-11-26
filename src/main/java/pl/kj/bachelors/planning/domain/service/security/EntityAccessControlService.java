package pl.kj.bachelors.planning.domain.service.security;

import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;

public interface EntityAccessControlService <T> {
    void ensureThatUserHasAccess(T subject, Object action) throws AccessDeniedException;
}
