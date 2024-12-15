package dev.matzat.numberconverter;

import dev.matzat.numberconverter.controller.ConverterController;
import dev.matzat.numberconverter.converter.BinaryToRomanConverter;
import dev.matzat.numberconverter.converter.ConverterResolver;
import dev.matzat.numberconverter.converter.DecimalToRomanConverter;
import dev.matzat.numberconverter.persistence.AuditLogRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor_ = @Autowired)
class NumberConverterApplicationTests extends SpringBootTestBase {

    private BinaryToRomanConverter binaryToRomanConverter;

    private DecimalToRomanConverter decimalToRomanConverter;

    private ConverterResolver converterResolver;

    private ConverterController converterController;

    private AuditLogRepository auditLogRepository;

    @Test
    void contextLoads() {
        assertThat(decimalToRomanConverter).isNotNull();
        assertThat(binaryToRomanConverter).isNotNull();
        assertThat(converterResolver).isNotNull();
        assertThat(converterController).isNotNull();
        assertThat(auditLogRepository).isNotNull();
    }
}
