package com.aaron.recipeweb.util;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.aaron.recipeweb.response.ResponseError;

import reactor.core.publisher.Mono;

public final class ResponseUtils
{
    private ResponseUtils()
    {
        throw new AssertionError("Utility class cannot be initialize");
    }

    public static Mono<DataBuffer> errorResponseBody(HttpStatus status, String message, ServerHttpResponse response)
    {
        response.setStatusCode(status);
        response.getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ResponseError body = ResponseError.builder()
                .message(message)
                .build();

        byte[] bodyBytes = JsonUtils.toBytes(body);
        DataBuffer dataBuffer = response.bufferFactory()
                .wrap(bodyBytes);

        return Mono.just(dataBuffer);
    }
}
