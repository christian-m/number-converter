package dev.matzat.numberconverter.converter;

abstract non-sealed class BaseConverter implements Converter {
    @Override
    public final String convert(final String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException(String.format("Submitted input '%s' is not a valid value or in the supported range",
                value));
        }
        return conversion(value);
    }

    abstract String conversion(String value);
}
