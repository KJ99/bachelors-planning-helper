package pl.kj.bachelors.planning.domain.model.payload.command;

import pl.kj.bachelors.planning.domain.model.extension.Estimation;

public class EstimationCommandPayload {
    private int itemId;
    private Estimation estimation;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }
}
