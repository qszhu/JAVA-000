package io.github.qszhu.gateway.upstream.client;

import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.io.IOException;

public interface UpstreamClient {
    HttpResponse request(UpstreamHost upstreamHost, HttpRequest req) throws IOException;
}
