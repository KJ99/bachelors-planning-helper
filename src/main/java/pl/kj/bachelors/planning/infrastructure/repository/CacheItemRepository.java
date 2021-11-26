package pl.kj.bachelors.planning.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kj.bachelors.planning.domain.model.entity.CacheItem;

import java.util.Optional;

public interface CacheItemRepository extends JpaRepository<CacheItem, Integer> {
    @Query(
            value = "select * from cache_items where " +
                    "tag = :tag and " +
                    "item_key = :key and " +
                    "expires_at > current_timestamp() " +
                    "order by created_at desc " +
                    "limit 1",
            nativeQuery = true
    )
    Optional<CacheItem> findFirstValidByTagAndKey(String tag, String key);
}
