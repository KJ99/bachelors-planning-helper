package pl.kj.bachelors.planning.domain.model.create;

import io.swagger.v3.oas.annotations.Hidden;
import org.codehaus.jackson.annotate.JsonIgnore;
import pl.kj.bachelors.planning.domain.constraint.FutureDateTime;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class PlanningCreateModel {
    @Positive(message = "PL.001")
    private Integer teamId;
    @NotBlank(message = "PL.002")
    private String title;
    @FutureDateTime(message = "PL.003")
    private String startDate;

    @JsonIgnore
    @Hidden
    private String timeZone;
    @JsonIgnore
    @Hidden
    private PlanningStatus status;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public PlanningStatus getStatus() {
        return status;
    }

    public void setStatus(PlanningStatus status) {
        this.status = status;
    }
}
