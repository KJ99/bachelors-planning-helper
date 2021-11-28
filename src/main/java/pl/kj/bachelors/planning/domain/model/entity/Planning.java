package pl.kj.bachelors.planning.domain.model.entity;

import pl.kj.bachelors.planning.domain.model.embeddable.Audit;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

@Entity
@Table(name = "plannings")
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "team_id")
    private int teamId;
    private String title;
    @Column(name = "start_at")
    private Calendar startAt;
    @Column(name = "timezone")
    private String timeZone;
    @Enumerated(EnumType.STRING)
    private PlanningStatus status;
    @Embedded
    private Audit audit;
    @OneToMany(mappedBy = "planning")
    private Set<PlanningItem> items;

    public Planning() {
        this.audit = new Audit();
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public PlanningStatus getStatus() {
        return status;
    }

    public void setStatus(PlanningStatus status) {
        this.status = status;
    }

    public Calendar getStartAt() {
        return startAt;
    }

    public void setStartAt(Calendar startAt) {
        this.startAt = startAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Set<PlanningItem> getItems() {
        return items;
    }

    public void setItems(Set<PlanningItem> items) {
        this.items = items;
    }

    public boolean hasStatusIn(List<PlanningStatus> statuses) {
        return statuses.stream().anyMatch(status -> status.equals(this.getStatus()));
    }
}
