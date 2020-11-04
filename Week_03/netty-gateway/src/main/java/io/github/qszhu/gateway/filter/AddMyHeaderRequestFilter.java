package io.github.qszhu.gateway.filter;

import io.netty.handler.codec.http.HttpRequest;

public class AddMyHeaderRequestFilter implements GatewayRequestFilter {

    @Override
    public HttpRequest filter(HttpRequest req) {
        req.headers().add("nio", "qinsi");
        return req;
    }
}
