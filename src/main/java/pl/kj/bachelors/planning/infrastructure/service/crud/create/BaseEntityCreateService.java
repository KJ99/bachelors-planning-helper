package pl.kj.bachelors.planning.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.service.ModelValidator;

public abstract class BaseEntityCreateService<E, PK, R extends JpaRepository<E, PK>, C> {
    protected final ModelMapper modelMapper;
    protected final R repository;
    protected final ModelValidator validator;

    protected BaseEntityCreateService(ModelMapper modelMapper, R repository, ModelValidator validator) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.validator = validator;
    }

    @Transactional(rollbackFor = { AccessDeniedException.class, AggregatedApiError.class })
    public E create(C model, Class<E> entityClass) throws Exception {
        this.ensureThatModelIsValid(model);
        this.preCreate(model);
        E entity = this.modelMapper.map(model, entityClass);
        this.repository.save(entity);
        this.postCreate(entity);

        return entity;
    }

    protected void postCreate(E entity) throws Exception {}
    protected void preCreate(C model) throws Exception {}

    protected void ensureThatModelIsValid(C model) throws AggregatedApiError {
        var violations = this.validator.validateModel(model);
        if(violations.size() > 0) {
            var ex = new AggregatedApiError();
            ex.setErrors(violations);
            throw ex;
        }
    }
}
