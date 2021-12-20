package pl.kj.bachelors.planning.domain.constraint;

import org.jboss.jandex.PrimitiveType;
import pl.kj.bachelors.planning.domain.validator.FromIntegerArrayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FromIntegerArrayValidator.class)
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromIntegerArray {
    int[] value();
    String message() default "Invalid value!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
