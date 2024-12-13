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
            Arguments.of(2, "II"),
            Arguments.of(4, "IV"),
            Arguments.of(5, "V"),
            Arguments.of(6, "VI"),
            Arguments.of(9, "IX"),
            Arguments.of(10, "X"),
            Arguments.of(11, "XI"),
            Arguments.of(40, "XL"),
            Arguments.of(49, "XLIX"),
            Arguments.of(50, "L"),
            Arguments.of(51, "LI"),
            Arguments.of(60, "LX"),
            Arguments.of(61, "LXI"),
            Arguments.of(90, "XC"),
            Arguments.of(99, "XCIX"),
            Arguments.of(100, "C"),
            Arguments.of(101, "CI"),
            Arguments.of(111, "CXI"),
            Arguments.of(150, "CL"),
            Arguments.of(151, "CLI"),
            Arguments.of(400, "CD"),
            Arguments.of(499, "CDXCIX"),
            Arguments.of(500, "D"),
            Arguments.of(501, "DI"),
            Arguments.of(510, "DX"),
            Arguments.of(511, "DXI"),
            Arguments.of(600, "DC"),
            Arguments.of(900, "CM"),
            Arguments.of(990, "CMXC"),
            Arguments.of(999, "CMXCIX"),
            Arguments.of(1000, "M"),
            Arguments.of(1001, "MI"),
            Arguments.of(1100, "MC"),
            Arguments.of(1101, "MCI"),
            Arguments.of(1104, "MCIV"),
            Arguments.of(1110, "MCX"),
            Arguments.of(3000, "MMM"),
            Arguments.of(3999, "MMMCMXCIX")
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
