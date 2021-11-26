package pl.kj.bachelors.planning.domain.service.crud.create;

public interface CreateService<E, C> {
    E create(C model, Class<E> entityClass) throws Exception;
}
