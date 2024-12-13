package dev.matzat.numberconverter.converter;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public final class ConverterResolver {

    // All known implementations of `Converter` will be injected here
    private final Set<Converter> converters;

    public ConverterResolver(final Set<Converter> convertersValue) {
        this.converters = Collections.unmodifiableSet(convertersValue);
    }

    public Converter resolve(final ConversionMethod conversionMethod) throws NoSuchElementException {
        return converters.stream().filter(converter -> converter.supports(conversionMethod)).findFirst().orElseThrow();
    }
}
