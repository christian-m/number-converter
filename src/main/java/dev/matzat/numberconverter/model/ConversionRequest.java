package dev.matzat.numberconverter.model;

import dev.matzat.numberconverter.converter.ConversionMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class ConversionRequest {
    @NotNull
    private ConversionMethod conversionMethod;
    @NotEmpty
    private String value;
}
