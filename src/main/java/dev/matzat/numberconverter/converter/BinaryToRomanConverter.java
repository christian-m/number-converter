package dev.matzat.numberconverter.converter;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class BinaryToRomanConverter implements Converter {

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
    public String convert(final String value) throws IllegalArgumentException {
        if (!isValid(value)) {
            throw new IllegalArgumentException(String.format("Submitted input '%s' is not a valid binary value or exceeds the limit of the maximum (%s)",
                value, Integer.toBinaryString(MAX_SUPPORTED_INPUT_VALUE)));
        }
        val input = Integer.parseInt(value, 2);
        return decimalToRomanConverter.convert(Integer.toString(input));
    }
}
