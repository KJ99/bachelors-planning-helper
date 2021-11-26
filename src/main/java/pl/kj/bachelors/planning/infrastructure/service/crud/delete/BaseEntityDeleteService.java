package pl.kj.bachelors.planning.infrastructure.service.crud.delete;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseEntityDeleteService<E, PK, R extends JpaRepository<E, PK>> {
    protected final R repository;

    protected BaseEntityDeleteService(R repository) {
        this.repository = repository;
    }

    public void delete(E entity) throws Exception {
        this.preDelete(entity);
        this.repository.delete(entity);
        this.postDelete(entity);
    }

    protected void preDelete(E entity) throws Exception {}
    protected void postDelete(E entity) throws Exception {}

}
