package pl.kj.bachelors.planning.domain.model.entity;

import pl.kj.bachelors.planning.domain.model.extension.Estimation;

import javax.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "estimation_value")
    @Enumerated(EnumType.STRING)
    private Estimation value;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private PlanningItem planningItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Estimation getValue() {
        return value;
    }

    public void setValue(Estimation value) {
        this.value = value;
    }

    public PlanningItem getPlanningItem() {
        return planningItem;
    }

    public void setPlanningItem(PlanningItem planningItem) {
        this.planningItem = planningItem;
    }
}
