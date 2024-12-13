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
    public String convert(final String value) throws IllegalArgumentException {
        try {
            val input = Integer.parseInt(value, 2);
            if (input > MAX_SUPPORTED_INPUT_VALUE) {
                throw new IllegalArgumentException("Binary to roman converter does not support converting larger binary values than 111110100000");
            }
            return decimalToRomanConverter.convert(Integer.toString(input));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Submitted input '%s' is not a valid binary value", value), e);
        }
    }
}
