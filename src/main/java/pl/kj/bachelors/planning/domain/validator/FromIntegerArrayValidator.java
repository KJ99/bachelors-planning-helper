package pl.kj.bachelors.planning.domain.validator;

import pl.kj.bachelors.planning.domain.constraint.FromIntegerArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FromIntegerArrayValidator implements ConstraintValidator<FromIntegerArray, Integer> {
    private int[] allowedValues;
    @Override
    public void initialize(FromIntegerArray constraintAnnotation) {
        this.allowedValues = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer o, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(allowedValues).anyMatch(o::equals);
    }
}
