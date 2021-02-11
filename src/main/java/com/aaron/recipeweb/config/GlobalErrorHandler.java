package com.aaron.recipeweb.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.aaron.recipeweb.response.ResponseError;
import com.aaron.recipeweb.util.JsonUtils;

import reactor.core.publisher.Mono;

@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler
{
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable)
    {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ResponseError body = ResponseError.builder()
                .message(throwable.getMessage())
                .build();
        byte[] bodyBytes = JsonUtils.toBytes(body);
        DataBuffer dataBuffer = response.bufferFactory()
                .wrap(bodyBytes);
        Mono<DataBuffer> monoBody = Mono.just(dataBuffer);

        return response.writeWith(monoBody);
    }
}