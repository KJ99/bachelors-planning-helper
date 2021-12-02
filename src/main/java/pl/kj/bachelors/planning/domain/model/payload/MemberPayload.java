package pl.kj.bachelors.planning.domain.model.payload;

public class MemberPayload {
    private String userId;

    public MemberPayload(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
