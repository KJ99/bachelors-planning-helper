package pl.kj.bachelors.planning.domain.model.create;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import org.codehaus.jackson.annotate.JsonIgnore;
import pl.kj.bachelors.planning.domain.constraint.FutureDateTime;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class PlanningCreateModel {
    @Positive(message = "PL.001")
    @CsvIgnore
    private Integer teamId;

    @NotBlank(message = "PL.002")
    @CsvBindByName(column = "Title", required = true)
    private String title;

    @FutureDateTime(message = "PL.003")
    @CsvBindByName(column = "Start Date")
    private String startDate;

    @JsonIgnore
    @Hidden
    @CsvIgnore
    private String timeZone;

    @JsonIgnore
    @Hidden
    @CsvIgnore
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
