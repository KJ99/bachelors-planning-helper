package pl.kj.bachelors.planning.domain.validator;

import pl.kj.bachelors.planning.domain.constraint.FutureDateTime;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FutureDateTimeValidator implements ConstraintValidator<FutureDateTime, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        boolean isValid;
        try {
            Date date = format.parse((String) o);
            TimeZone timeZone = RequestHandler.getRequestTimeZone().orElse(TimeZone.getDefault());
            isValid = date.getTime() > Calendar.getInstance(timeZone).getTimeInMillis();
        } catch (ParseException e) {
            isValid = false;
        }

        return isValid;
    }
}
