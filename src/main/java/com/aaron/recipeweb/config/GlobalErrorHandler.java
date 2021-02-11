package com.aaron.recipeweb.config;

import static org.springframework.http.HttpStatus.*;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.aaron.recipeweb.exception.NotFoundException;
import com.aaron.recipeweb.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler
{
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable)
    {
        log.error("handle. Error.", throwable);

        HttpStatus status;
        if(throwable instanceof NotFoundException)
        {
            status = NOT_FOUND;
        }
        else
        {
            status = INTERNAL_SERVER_ERROR;
        }

        ServerHttpResponse response = serverWebExchange.getResponse();
        Mono<DataBuffer> monoBody = ResponseUtils.errorResponseBody(status, throwable.getMessage(), response);

        return response.writeWith(monoBody);
    }
}