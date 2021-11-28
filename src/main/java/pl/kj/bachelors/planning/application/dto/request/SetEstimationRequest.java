package pl.kj.bachelors.planning.application.dto.request;

import pl.kj.bachelors.planning.domain.constraint.FromIntegerArray;

public class SetEstimationRequest {
    @FromIntegerArray(value = { 0, 1, 2, 3, 5, 8, 13, 21  }, message = "PL.121")
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
