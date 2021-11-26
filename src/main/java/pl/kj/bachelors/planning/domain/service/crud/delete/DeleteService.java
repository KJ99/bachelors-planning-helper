package pl.kj.bachelors.planning.domain.service.crud.delete;

public interface DeleteService<E, PK> {
    void delete(E entity) throws Exception;
}
