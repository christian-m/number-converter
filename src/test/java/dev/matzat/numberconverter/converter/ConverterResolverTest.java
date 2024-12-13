package dev.matzat.numberconverter.converter;

import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@Import({ConverterResolver.class, DecimalToRomanConverter.class, BinaryToRomanConverter.class})
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ConverterResolverTest {

    private ConverterResolver converterResolver;

    @Test
    @DisplayName("GIVEN a decimal to roman converter is registered " +
        "WHEN a decimal to roman converter is requested " +
        "THEN a decimal to roman converter instance is returned")
    public void resolveDecimalToRomanConverterTest() {
        val converter = converterResolver.resolve(ConversionMethod.DECIMAL_TO_ROMAN);
        assertThat(converter).isInstanceOf(DecimalToRomanConverter.class);
    }

    @Test
    @DisplayName("GIVEN a binary to roman converter is registered " +
        "WHEN a binary to roman converter is requested " +
        "THEN a binary to roman converter instance is returned")
    public void resolveBinaryToRomanConverterTest() {
        val converter = converterResolver.resolve(ConversionMethod.BINARY_TO_ROMAN);
        assertThat(converter).isInstanceOf(BinaryToRomanConverter.class);
    }

    @Test
    @DisplayName("GIVEN a decimal to roman converter is registered " +
        "WHEN a binary to roman converter is requested " +
        "THEN a NoSuchElementException is thrown")
    public void unknownConverterTest() {
        val converterResolver = new ConverterResolver(Set.of(new DecimalToRomanConverter()));
        assertThatThrownBy(() -> converterResolver.resolve(ConversionMethod.BINARY_TO_ROMAN)).isInstanceOf(NoSuchElementException.class);
    }
}
