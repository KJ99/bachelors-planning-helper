package pl.kj.bachelors.planning.domain.model.payload;

import pl.kj.bachelors.planning.domain.model.extension.Estimation;

public class PlanningItemPayload {
    private int id;
    private String title;
    private String description;
    private Estimation estimation;

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

    public Estimation getEstimation() {
        return estimation;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }
}
