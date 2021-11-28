package pl.kj.bachelors.planning.application.dto.request;

import pl.kj.bachelors.planning.domain.constraint.FromEnum;
import pl.kj.bachelors.planning.domain.constraint.FromIntegerArray;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class EstimationRequest {
    @FromEnum(enumClass = Estimation.class, message = "PL.121")
    @Enumerated(EnumType.STRING)
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
