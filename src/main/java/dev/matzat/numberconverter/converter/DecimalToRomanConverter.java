package dev.matzat.numberconverter.converter;

import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public final class DecimalToRomanConverter {

    private static final int MIN_SUPPORTED_INPUT_VALUE = 0;
    private static final int MAX_SUPPORTED_INPUT_VALUE = 3999;

    public String convert(final int inputNumber) throws IllegalArgumentException {
        var input = validateInput(inputNumber);
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

    private int validateInput(final int input) {
        if (input < MIN_SUPPORTED_INPUT_VALUE || input > MAX_SUPPORTED_INPUT_VALUE) {
            throw new IllegalArgumentException("Decimal to roman converter does not support converting larger numbers than 3999 or negative numbers");
        }
        return input;
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
