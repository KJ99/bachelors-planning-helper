package pl.kj.bachelors.planning.domain.model.payload.message;

import pl.kj.bachelors.planning.domain.model.extension.Estimation;

public class VotePayload {
    private String userId;
    private Estimation estimation;

    public VotePayload(String userId, Estimation estimation) {
        this.userId = userId;
        this.estimation = estimation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }
}
