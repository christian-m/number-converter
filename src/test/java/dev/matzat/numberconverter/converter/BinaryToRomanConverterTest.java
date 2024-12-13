package dev.matzat.numberconverter.converter;

import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@Import({BinaryToRomanConverter.class, DecimalToRomanConverter.class})
@AllArgsConstructor(onConstructor_ = @Autowired)
public class BinaryToRomanConverterTest {

    private BinaryToRomanConverter converter;

    private static Stream<Arguments> binaryToRomanTestdata() {
        return Stream.of(
            Arguments.of("00", ""),
            Arguments.of("01", "I"),
            Arguments.of("10", "II"),
            Arguments.of("0100", "IV"),
            Arguments.of("0101", "V"),
            Arguments.of("0110", "VI"),
            Arguments.of("1001", "IX"),
            Arguments.of("1010", "X"),
            Arguments.of("1011", "XI"),
            Arguments.of("101000", "XL"),
            Arguments.of("110001", "XLIX"),
            Arguments.of("110010", "L"),
            Arguments.of("110011", "LI"),
            Arguments.of("111100", "LX"),
            Arguments.of("111101", "LXI"),
            Arguments.of("01011010", "XC"),
            Arguments.of("01100011", "XCIX"),
            Arguments.of("01100100", "C"),
            Arguments.of("01100101", "CI"),
            Arguments.of("01101111", "CXI"),
            Arguments.of("10010110", "CL"),
            Arguments.of("10010111", "CLI"),
            Arguments.of("0110010000", "CD"),
            Arguments.of("0111110011", "CDXCIX"),
            Arguments.of("0111110100", "D"),
            Arguments.of("0111110101", "DI"),
            Arguments.of("0111111110", "DX"),
            Arguments.of("0111111111", "DXI"),
            Arguments.of("1001011000", "DC"),
            Arguments.of("1110000100", "CM"),
            Arguments.of("1111011110", "CMXC"),
            Arguments.of("1111100111", "CMXCIX"),
            Arguments.of("1111101000", "M"),
            Arguments.of("1111101001", "MI"),
            Arguments.of("010001001100", "MC"),
            Arguments.of("010001001101", "MCI"),
            Arguments.of("010001010000", "MCIV"),
            Arguments.of("010001010110", "MCX"),
            Arguments.of("101110111000", "MMM"),
            Arguments.of("111110011111", "MMMCMXCIX")
        );
    }

    @ParameterizedTest
    @MethodSource("binaryToRomanTestdata")
    @DisplayName("WHEN a valid binary value is submitted to the converter THEN the appropriate roman number is returned as string")
    public void testIntToRoman(String input, String expected) {
        val result = converter.convert(input);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("WHEN a binary value out of range is submitted to the converter THEN an IllegalArgumentException is thrown")
    public void testBinaryToRomanOutOfRange() {
        assertThatThrownBy(() -> converter.convert("111110100000")).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Binary to roman converter does not support converting larger binary values than 111110100000");
    }

    @Test
    @DisplayName("WHEN an invalid binary value is submitted to the converter THEN an IllegalArgumentException is thrown")
    public void testInvalidBinaryToRoman() {
        assertThatThrownBy(() -> converter.convert("1X0X1X0X")).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Submitted input '1X0X1X0X' is not a valid binary value");
    }
}
