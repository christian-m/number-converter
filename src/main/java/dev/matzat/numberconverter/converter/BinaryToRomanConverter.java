package dev.matzat.numberconverter.converter;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class BinaryToRomanConverter extends BaseConverter {

    private static final int MAX_SUPPORTED_INPUT_VALUE = 3999;

    private DecimalToRomanConverter decimalToRomanConverter;

    @Override
    public boolean supports(final ConversionMethod conversionMethod) {
        return conversionMethod == ConversionMethod.BINARY_TO_ROMAN;
    }

    @Override
    public boolean isValid(final String value) {
        if (!value.matches("[01]+")) {
            return false;
        }
        try {
            val input = Integer.parseInt(value, 2);
            return (input <= MAX_SUPPORTED_INPUT_VALUE);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String conversion(final String value) throws IllegalArgumentException {
        val input = Integer.parseInt(value, 2);
        return decimalToRomanConverter.convert(Integer.toString(input));
    }
}
