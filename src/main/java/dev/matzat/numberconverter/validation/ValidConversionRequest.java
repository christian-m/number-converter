package dev.matzat.numberconverter.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ConversionRequestValidator.class})
public @interface ValidConversionRequest {
    String message() default "{dev.matzat.numberconverter.validation.ConversionRequestValidation.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

