package dev.matzat.numberconverter.validation;

import dev.matzat.numberconverter.converter.ConverterResolver;
import dev.matzat.numberconverter.model.ConversionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.val;

import java.util.NoSuchElementException;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public final class ConversionRequestValidator implements ConstraintValidator<ValidConversionRequest, ConversionRequest> {

    private static final String CONVERTER_NOT_FOUND_MESSAGE_TEMPLATE = "{dev.matzat.numberconverter.validation.ConversionRequestValidation.converterNotFound}";

    public ConversionRequestValidator(final ConverterResolver converterResolver) {
        this.converterResolver = converterResolver;
    }

    private final ConverterResolver converterResolver;

    private ValidConversionRequest annotation = null;

    @Override
    public void initialize(final ValidConversionRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(final ConversionRequest conversionRequest, final ConstraintValidatorContext context) {
        try {
            val converter = converterResolver.resolve(conversionRequest.getConversionMethod());
            if (converter.isValid(conversionRequest.getValue())) {
                return true;
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.annotation.message())
                .addPropertyNode("value")
                .addConstraintViolation();
            return false;
        } catch (NoSuchElementException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(CONVERTER_NOT_FOUND_MESSAGE_TEMPLATE)
                .addPropertyNode("conversionMethod")
                .addConstraintViolation();
            return false;
        }
    }
}
