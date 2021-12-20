package pl.kj.bachelors.planning.domain.model.update;

import javax.validation.constraints.NotBlank;

public class PlanningItemUpdateModel {
    @NotBlank(message = "PL.011")
    private String title;
    private String description;

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
}
