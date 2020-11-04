package io.github.qszhu.gateway.filter;

import io.netty.handler.codec.http.HttpRequest;

public interface GatewayRequestFilter {
    HttpRequest filter(HttpRequest req);
}
