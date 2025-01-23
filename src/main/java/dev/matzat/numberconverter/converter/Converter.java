package dev.matzat.numberconverter.converter;

public sealed interface Converter permits BaseConverter {
    boolean supports(ConversionMethod conversionMethod);

    boolean isValid(String value);

    String convert(String value);
}
