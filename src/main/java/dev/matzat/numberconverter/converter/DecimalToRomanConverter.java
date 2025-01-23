package dev.matzat.numberconverter.converter;

import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public final class DecimalToRomanConverter extends BaseConverter {

    private static final int MIN_SUPPORTED_INPUT_VALUE = 0;
    private static final int MAX_SUPPORTED_INPUT_VALUE = 3999;

    @Override
    public boolean supports(final ConversionMethod conversionMethod) {
        return conversionMethod == ConversionMethod.DECIMAL_TO_ROMAN;
    }

    @Override
    public boolean isValid(final String value) {
        if (!value.matches("[0-9]+")) {
            return false;
        }
        try {
            val intValue = Integer.parseInt(value);
            return intValue >= MIN_SUPPORTED_INPUT_VALUE && intValue <= MAX_SUPPORTED_INPUT_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String conversion(final String value) throws IllegalArgumentException {
        var input = Integer.parseInt(value);
        val roman = new StringBuilder();
        for (RomanBase conversionBase : RomanBase.values()) {
            if (input < conversionBase.getBase()) {
                continue;
            }
            roman.append(conversionBase.name().repeat(Math.max(0, input / conversionBase.getBase())));
            input = input % conversionBase.getBase();
        }
        return roman.toString();
    }

    @Getter
    private enum RomanBase {
        M(1000),
        CM(900),
        D(500),
        CD(400),
        C(100),
        XC(90),
        L(50),
        XL(40),
        X(10),
        IX(9),
        V(5),
        IV(4),
        I(1);

        private final int base;

        RomanBase(final int baseValue) {
            this.base = baseValue;
        }
    }
}
