package pl.kj.bachelors.planning.application.dto.response.vote;

public class VoteResponse {
    private String userId;
    private String value;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
