package io.github.qszhu.gateway;

import io.github.qszhu.gateway.filter.GatewayRequestFilter;
import io.github.qszhu.gateway.filter.GatewayRequestFilters;
import io.github.qszhu.gateway.router.GatewayRouter;
import io.github.qszhu.gateway.upstream.client.UpstreamClient;
import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;

public class GatewayHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final UpstreamClient upstreamClient;

    public GatewayHandler(UpstreamClient upstreamClient) {
        this.upstreamClient = upstreamClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (!(msg instanceof HttpRequest)) return;

        HttpRequest req = (HttpRequest) msg;

        // request filters
        for (GatewayRequestFilter filter : GatewayRequestFilters.getInstance()) {
            req = filter.filter(req);
        }

        // router
        UpstreamHost upstreamHost = GatewayRouter.getInstance().choose();

        // do request
        HttpResponse resp = upstreamClient.request(upstreamHost, req);

        // TODO: response filters

        // keep-alive things
        boolean keepAlive = HttpUtil.isKeepAlive(req);

        if (keepAlive) {
            if (!req.protocolVersion().isKeepAliveDefault()) {
                resp.headers().set(CONNECTION, KEEP_ALIVE);
            }
        } else {
            resp.headers().set(CONNECTION, CLOSE);
        }

        ChannelFuture f = ctx.write(resp);

        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
