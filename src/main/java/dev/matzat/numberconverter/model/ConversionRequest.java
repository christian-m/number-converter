package dev.matzat.numberconverter.model;

import dev.matzat.numberconverter.converter.ConversionMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ConversionRequest(
    @NotNull
    ConversionMethod conversionMethod,
    @NotEmpty
    String value
) {
}
