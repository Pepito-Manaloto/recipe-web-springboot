package com.aaron.recipeweb.resource.filter;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import com.aaron.recipeweb.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationFilter implements WebFilter
{
    @Value("${web.authorization-header}")
    private String authorizationHeader;

    private String authorizationHeaderMD5;
    private PathPattern pathToApplyFilter;

    @PostConstruct
    public void init()
    {
        authorizationHeaderMD5 = md5DigestAsHex(authorizationHeader.getBytes());
        pathToApplyFilter = new PathPatternParser().parse("/web_service/*");

        log.info("init. Initialized. authorizationHeaderMD5={}", authorizationHeaderMD5);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain)
    {
        PathContainer path = exchange.getRequest()
                .getPath()
                .pathWithinApplication();
        boolean willApplyFilter = pathToApplyFilter.matches(path);

        log.info("filter. Start. willApplyFilter={} path={}", willApplyFilter, path);

        if(willApplyFilter)
        {
            return validateAuthorizationHeader(exchange, chain);
        }
        else
        {
            return chain.filter(exchange);
        }
    }

    private Mono<Void> validateAuthorizationHeader(ServerWebExchange exchange, WebFilterChain chain)
    {
        String authorization = exchange.getRequest()
                .getHeaders()
                .getFirst(AUTHORIZATION);

        log.info("filter. Checking. authorizationHeaderMD5={} authorization={}", authorizationHeaderMD5, authorization);

        if(isNotBlank(authorization))
        {
            boolean isAuthorized = authorizationHeaderMD5.equals(authorization);
            if(isAuthorized)
            {
                return chain.filter(exchange);
            }
            else
            {
                return error(exchange.getResponse(), UNAUTHORIZED, "Request not authorized.");
            }
        }
        else
        {
            return error(exchange.getResponse(), BAD_REQUEST, "Authorization header is required.");
        }
    }

    private Mono<Void> error(ServerHttpResponse response, HttpStatus status, String errorMessage)
    {
        Mono<DataBuffer> monoBody = ResponseUtils.errorResponseBody(status, errorMessage, response);

        return response.writeWith(monoBody);
    }
}
