package pl.kj.bachelors.planning.domain.model.extension;

public enum RequestAttributeName {
    USER_ID("uid"),
    PLANNING_ID("planning_id");

    public final String value;

    RequestAttributeName(String value) {
        this.value = value;
    }
}
