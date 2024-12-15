package dev.matzat.numberconverter.controller.config;

import dev.matzat.numberconverter.persistence.AuditLog;
import dev.matzat.numberconverter.persistence.AuditLogRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.time.Clock;

@Component
@AllArgsConstructor
@Slf4j
@SuppressWarnings("DesignForExtension")
public class AuditWebFilter implements WebFilter {

    private Clock clock;
    private AuditLogRepository auditLogRepository;

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        val timestamp = clock.instant();

        ServerHttpRequest request = exchange.getRequest();
        RequestDecorator requestDecorator = new RequestDecorator(request);

        ServerHttpResponse response = exchange.getResponse();
        ResponseDecorator responseDecorator = new ResponseDecorator(response, request);

        return chain.filter(new ServerWebExchangeDecorator(exchange) {
            @Override
            public ServerHttpResponse getResponse() {
                return responseDecorator;
            }

            @Override
            public ServerHttpRequest getRequest() {
                return requestDecorator;
            }
        }).doFinally(signalType -> {
            val uriPath = requestDecorator.getURI().getPath();
            if (uriPath.startsWith("/convert")) {
                val input = requestDecorator.getRequestBody();
                val output = getResponseBody(responseDecorator);
                val responseStatusCode = response.getStatusCode();
                @SuppressWarnings("AvoidInlineConditionals")
                val statusCode = responseStatusCode != null ? responseStatusCode.toString() : "unknown";
                val success = responseDecorator.success();
                log.debug("Request Body: {}", input);
                log.debug("Response Body: {}", output);
                log.debug("Response Status: {}", statusCode);
                auditLogRepository.save(new AuditLog(timestamp, input, output, statusCode, success));
            }
        });
    }

    private static String getResponseBody(final ResponseDecorator responseDecorator) {
        return responseDecorator.getResponseBody();
    }
}

@Slf4j
@Getter
class ResponseDecorator extends ServerHttpResponseDecorator {

    private final ServerHttpRequest request;
    private String responseBody;

    ResponseDecorator(final ServerHttpResponse response, final ServerHttpRequest request) {
        super(response);
        this.request = request;
    }

    @Override
    public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {
        return super.writeWith(Flux.from(body)
            .doOnNext(responseBuffer -> {
                    val uriPath = request.getURI().getPath();
                    if (uriPath.startsWith("/convert")) {
                        try {
                            val bodyStream = new ByteArrayOutputStream();
                            Channels.newChannel(bodyStream).write(responseBuffer.asByteBuffer().asReadOnlyBuffer());
                            responseBody = bodyStream.toString(StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            log.debug("parsing response body failed", e);
                        }
                    }
                }
            ));
    }

    public boolean success() {
        return getStatusCode() == HttpStatus.OK;
    }
}


@Slf4j
@Getter
class RequestDecorator extends ServerHttpRequestDecorator {

    private String requestBody;

    RequestDecorator(final ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(requestBuffer -> {
            try {
                val bodyStream = new ByteArrayOutputStream();
                Channels.newChannel(bodyStream).write(requestBuffer.asByteBuffer().asReadOnlyBuffer());
                requestBody = bodyStream.toString(StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.debug("parsing request body failed", e);
            }
        });
    }
}
