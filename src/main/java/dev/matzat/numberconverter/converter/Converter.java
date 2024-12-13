package dev.matzat.numberconverter.converter;

public interface Converter {
    boolean isValid(String value);

    String convert(String value);

    boolean supports(ConversionMethod conversionMethod);
}
