package pl.kj.bachelors.planning.domain.model.create;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import org.codehaus.jackson.annotate.JsonIgnore;
import pl.kj.bachelors.planning.domain.model.entity.Planning;

import javax.validation.constraints.NotBlank;

public class PlanningItemCreateModel {
    @NotBlank(message = "PL.002")
    @CsvBindByName
    private String title;
    @CsvBindByName
    private String description;

    @JsonIgnore
    @Hidden
    @CsvIgnore
    private Planning planning;

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

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }
}
