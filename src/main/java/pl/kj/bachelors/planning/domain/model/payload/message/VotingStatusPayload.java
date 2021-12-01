package pl.kj.bachelors.planning.domain.model.payload.message;

public class VotingStatusPayload {
    private boolean enabled;

    public VotingStatusPayload(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
