package io.github.qszhu.gateway;

import io.github.qszhu.gateway.upstream.client.OkUpstreamClient;
import io.github.qszhu.gateway.upstream.client.UpstreamClient;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

public class GatewayInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        UpstreamClient client = new OkUpstreamClient();

        ch.pipeline().addLast(
                new HttpServerCodec(),
                new HttpServerExpectContinueHandler(),
                new GatewayHandler(client));
    }
}
