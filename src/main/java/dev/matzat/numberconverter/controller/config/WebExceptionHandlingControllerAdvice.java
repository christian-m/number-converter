package dev.matzat.numberconverter.controller.config;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
class WebExceptionHandlingControllerAdvice {

    private static final String DESCRIPTION_400 = "The general request body is or parameters are invalid";
    private static final String DESCRIPTION_422 = "Provided object is not valid";
    private static final String DESCRIPTION_500 = "Unexpected Error";

    private MessageSource messageSource;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ApiResponse(
        responseCode = "422",
        description = DESCRIPTION_422,
        content = {
            @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class)
            )
        }
    )
    public ProblemDetail handleIllegalArgumentException(final IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage());
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
        responseCode = "400",
        description = DESCRIPTION_400,
        content = {
            @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class)
            )
        }
    )
    public ProblemDetail handleServerWebInputException(final ServerWebInputException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ApiResponse(
        responseCode = "422",
        description = DESCRIPTION_422,
        content = {

            @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class)
            ),
        }
    )
    @SuppressWarnings("unused")
    public ProblemDetail handleNoSuchElementException(final NoSuchElementException exception) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            messageSource.getMessage(
                "dev.matzat.numberconverter.controller.config.WebExceptionHandlingControllerAdvice.converterNotFound",
                new String[0],
                LocaleContextHolder.getLocale())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ApiResponse(
        responseCode = "422",
        description = DESCRIPTION_422,
        content = {
            @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class)
            )
        }
    )
    public ProblemDetail handleConstraintViolationException(final ConstraintViolationException exception) {
        val fieldErrors = exception.getConstraintViolations()
            .stream().map(it -> new FieldError(it.getRootBean().toString(), it.getPropertyPath().toString(), it.getMessage())).toList();
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, fieldErrorsToString(fieldErrors));
        problemDetail.setProperties(Map.of("fieldErrorDetails", fieldErrorsToMap(fieldErrors)));
        return problemDetail;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(
        responseCode = "500",
        description = DESCRIPTION_500,
        content = {

            @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class)
            ),
        }
    )
    public ProblemDetail handleThrowable(final Throwable exception) {
        val exceptionName = exception.getClass().getName();
        log.error("An unexpected error occurred: {}", exceptionName, exception);
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            messageSource.getMessage(
                "dev.matzat.numberconverter.controller.config.WebExceptionHandlingControllerAdvice.unexpectedError",
                new String[]{exceptionName},
                LocaleContextHolder.getLocale())
        );
    }

    private String fieldErrorsToString(final List<FieldError> fieldErrors) {
        return String.join(", ", fieldErrors.stream().map((it) ->
            String.format("%s: %s", it.getField(), it.getDefaultMessage())).collect(Collectors.toSet()));
    }

    private Map<String, Set<String>> fieldErrorsToMap(final List<FieldError> fieldErrors) {
        return fieldErrors.stream().collect(Collectors.groupingBy(
            FieldError::getField,
            Collectors.mapping(
                FieldError::getDefaultMessage, Collectors.toSet())));
    }
}
