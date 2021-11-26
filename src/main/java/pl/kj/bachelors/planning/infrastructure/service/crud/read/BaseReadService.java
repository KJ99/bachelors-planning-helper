package pl.kj.bachelors.planning.infrastructure.service.crud.read;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class BaseReadService<E, PK, R extends JpaRepository<E, PK>, V> {
    protected final R repository;

    protected BaseReadService(R repository) {
        this.repository = repository;
    }

    public Page<V> readPaged(Pageable query) {
        return this.repository.findAll(query).map(this::createResult);
    }

    public Optional<V> readParticular(PK identity) {
        Optional<E> data = this.repository.findById(identity);
        return data.map(this::createResult);
    }

    protected abstract V createResult(E source);
}
