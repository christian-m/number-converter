package dev.matzat.numberconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
public class ConversionServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ConversionServiceApplication.class, args);
    }
}
