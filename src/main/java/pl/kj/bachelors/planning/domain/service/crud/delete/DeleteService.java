package pl.kj.bachelors.planning.domain.service.crud.delete;

public interface DeleteService<E> {
    void delete(E entity) throws Exception;
}
