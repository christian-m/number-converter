package dev.matzat.numberconverter;

import dev.matzat.numberconverter.converter.BinaryToRomanConverter;
import dev.matzat.numberconverter.converter.ConverterResolver;
import dev.matzat.numberconverter.converter.DecimalToRomanConverter;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AllArgsConstructor(onConstructor_ = @Autowired)
class ConversionServiceApplicationTests {

    private BinaryToRomanConverter binaryToRomanConverter;

    private DecimalToRomanConverter decimalToRomanConverter;

    private ConverterResolver converterResolver;

    @Test
    void contextLoads() {
        assertThat(decimalToRomanConverter).isNotNull();
        assertThat(binaryToRomanConverter).isNotNull();
        assertThat(converterResolver).isNotNull();
    }
}
