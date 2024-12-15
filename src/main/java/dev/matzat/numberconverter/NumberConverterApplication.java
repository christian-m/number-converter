package dev.matzat.numberconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
@SuppressWarnings({"HideUtilityClassConstructor", "DesignForExtension"})
public class NumberConverterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(NumberConverterApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean(Clock.class)
    public Clock providingBasicClock() {
        return Clock.systemUTC();
    }
}
