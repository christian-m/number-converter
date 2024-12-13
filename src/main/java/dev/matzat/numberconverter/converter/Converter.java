package dev.matzat.numberconverter.converter;

public interface Converter {
    String convert(String value);

    boolean supports(ConversionMethod conversionMethod);
}
