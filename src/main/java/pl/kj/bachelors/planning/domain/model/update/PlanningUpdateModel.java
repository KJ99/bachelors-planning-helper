package pl.kj.bachelors.planning.domain.model.update;

import io.swagger.v3.oas.annotations.Hidden;
import org.codehaus.jackson.annotate.JsonIgnore;
import pl.kj.bachelors.planning.domain.constraint.FutureDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class PlanningUpdateModel {
    @NotBlank(message = "PL.002")
    private String title;
    @FutureDateTime(message = "PL.003")
    private String startDate;

    @JsonIgnore
    @Hidden
    private String timeZone;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
