package pl.kj.bachelors.planning.domain.service.crud.read;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReadService<T, PK, V> {
    Page<V> readPaged(Pageable query);
    Optional<V> readParticular(PK identity);
}
