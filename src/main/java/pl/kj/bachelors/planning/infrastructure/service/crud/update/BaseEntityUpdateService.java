package pl.kj.bachelors.planning.infrastructure.service.crud.update;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.infrastructure.service.ValidationService;

import java.util.Collection;

public abstract class BaseEntityUpdateService<T, PK, U, R extends JpaRepository<T, PK>> {
    protected final R repository;
    protected final ValidationService validationService;
    protected final ModelMapper modelMapper;
    protected final ObjectMapper objectMapper;

    protected BaseEntityUpdateService(
            R repository,
            ValidationService validationService,
            ModelMapper modelMapper,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.validationService = validationService;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    @Transactional(rollbackFor = ApiError.class)
    public void processUpdate(T original, JsonPatch patch, Class<U> updateModelClass)
            throws Exception {
        U modelFromOriginal = this.modelMapper.map(original, updateModelClass);
        JsonNode patched = patch.apply(objectMapper.convertValue(modelFromOriginal, JsonNode.class));

        U updateModel = this.objectMapper.treeToValue(patched, updateModelClass);
        this.ensureThatModelIsValid(updateModel);

        this.preUpdate(original, updateModel);
        this.applyUpdateModel(original, updateModel);
        this.postUpdate(original);

        this.repository.save(original);
    }

    private <V> void ensureThatModelIsValid(V model) throws AggregatedApiError {
        Collection<ApiError> violations = this.validationService.validateModel(model);
        if(violations.size() > 0) {
            AggregatedApiError ex = new AggregatedApiError();
            ex.setErrors(violations);

            throw ex;
        }
    }

    protected void preUpdate(T original, U updateModel) throws Exception {}
    protected void postUpdate(T entity) throws Exception {}

    protected void applyUpdateModel(T original, U updateModel) {
        this.modelMapper.map(updateModel, original);
    }
}
