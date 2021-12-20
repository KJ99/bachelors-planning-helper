package pl.kj.bachelors.planning.domain.model.report;

import pl.kj.bachelors.planning.domain.annotation.PdfColumn;

public class PlanningItemReport {
    @PdfColumn(name = "Title", relativeWidth = 4f)
    private String title;
    @PdfColumn(name = "Estimation")
    private String estimation;
    @PdfColumn(name = "Story Points")
    private int storyPoints;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }
}
