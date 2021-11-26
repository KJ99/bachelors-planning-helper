package pl.kj.bachelors.planning.domain.service.crud.update;

import com.github.fge.jsonpatch.JsonPatch;

public interface UpdateService<T, U> {
    void processUpdate(T original, JsonPatch patch, Class<U> updateModelClass) throws Exception;
}
