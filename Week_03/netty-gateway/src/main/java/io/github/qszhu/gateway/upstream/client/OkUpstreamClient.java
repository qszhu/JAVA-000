package io.github.qszhu.gateway.upstream.client;

import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import kotlin.Pair;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class OkUpstreamClient implements UpstreamClient {
    private final OkHttpClient client;

    public OkUpstreamClient() {
        client = new OkHttpClient();
    }

    @Override
    public HttpResponse request(UpstreamHost upstreamHost, HttpRequest req) throws IOException {
        String url = String.format("%s%s", upstreamHost.url, req.uri());
        System.out.println(url);

        Request.Builder builder = new Request.Builder().url(url);

        for (Map.Entry<String, String> header : req.headers().entries()) {
            if ("host".equals(header.getKey().toLowerCase())) continue;
            System.out.printf("%s %s\n", header.getKey(), header.getValue());
            builder.addHeader(header.getKey(), header.getValue());
        }

        Request okReq = builder.build();

        try (Response okResp = client.newCall(okReq).execute()) {
            HttpResponseStatus status = new HttpResponseStatus(okResp.code(), okResp.message());
            byte[] body = Objects.requireNonNull(okResp.body()).bytes();

            FullHttpResponse resp = new DefaultFullHttpResponse(
                    req.protocolVersion(), status, Unpooled.wrappedBuffer(body));

            for (Pair<? extends String, ? extends String> header : okResp.headers()) {
                resp.headers().set(header.getFirst(), header.getSecond());
            }

            return resp;
        }
    }
}
