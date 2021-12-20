package pl.kj.bachelors.planning.application.dto.request;

public class ChangeVotingStatusRequest {
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
