package dev.matzat.numberconverter.converter;

import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@Import(DecimalToRomanConverter.class)
@AllArgsConstructor(onConstructor_ = @Autowired)
public class DecimalToRomanConverterTest {

    private DecimalToRomanConverter converter;

    private static Stream<Arguments> intToRomanTestdata() {
        return Stream.of(
                Arguments.of(0, ""),
                Arguments.of(1, "I"),
                Arguments.of(5, "V"),
                Arguments.of(10, "X"),
                Arguments.of(50, "L"),
                Arguments.of(100, "C"),
                Arguments.of(500, "D"),
                Arguments.of(1000, "M"),
                Arguments.of(3000, "MMM")
        );
    }

    @ParameterizedTest
    @MethodSource("intToRomanTestdata")
    @DisplayName("WHEN a valid integer is submitted to the converter THEN the appropriate roman number is returned as string")
    public void testIntToRoman(int input, String expected) {
        val result = converter.convert(input);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "4000"})
    @DisplayName("WHEN an integer out of range is submitted to the converter THEN an IllegalArgumentException is thrown")
    public void testIntToRomanValueOutOfRange(int input) {
        assertThatThrownBy(() -> converter.convert(input)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Decimal to roman converter does not support converting larger numbers than 3999 or negative numbers");
    }
}
