package pl.kj.bachelors.planning.domain.model.entity;

import pl.kj.bachelors.planning.domain.model.embeddable.Audit;
import pl.kj.bachelors.planning.domain.model.extension.CacheTag;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "cache_items")
public class CacheItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "item_key")
    private String key;
    @Enumerated(EnumType.STRING)
    private CacheTag tag;
    @Column(name = "item_value")
    private String value;
    @Embedded
    private Audit audit;
    @Column(name = "expires_at")
    private Calendar expiresAt;

    public CacheItem() {
        this.audit = new Audit();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CacheTag getTag() {
        return tag;
    }

    public void setTag(CacheTag tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Calendar getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Calendar expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
