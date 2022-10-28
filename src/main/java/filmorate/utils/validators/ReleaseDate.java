package filmorate.utils.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.temporal.Temporal;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateReleaseValidator.class)
@Documented
public @interface ReleaseDate {
    String message() default "Дата выхода фильма должна быть после 28.12.1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class DateReleaseValidator implements ConstraintValidator<ReleaseDate, Temporal> {
    public static final LocalDate RELEASE_START = LocalDate.of(1895, 12, 28);
    @Override
    public void initialize(ReleaseDate constraintAnnotation) {}

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        return value == null || LocalDate.from(value).isAfter(RELEASE_START);
    }
}