package pl.kj.bachelors.planning.domain.model.report;

import java.util.List;

public class PlanningReport {
    private String title;
    private String teamName;
    private String date;
    private List<PlanningItemReport> results;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PlanningItemReport> getResults() {
        return results;
    }

    public void setResults(List<PlanningItemReport> results) {
        this.results = results;
    }
}
