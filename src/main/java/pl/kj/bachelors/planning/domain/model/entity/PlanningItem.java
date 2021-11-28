package pl.kj.bachelors.planning.domain.model.entity;

import pl.kj.bachelors.planning.domain.model.embeddable.Audit;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
public class PlanningItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private boolean focused = false;
    @ManyToOne
    @JoinColumn(name = "planning_id", referencedColumnName = "id")
    private Planning planning;
    private Audit audit;
    private Integer estimation;

    public PlanningItem() {
        this.audit = new Audit();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Integer getEstimation() {
        return estimation;
    }

    public void setEstimation(Integer estimation) {
        this.estimation = estimation;
    }
}
