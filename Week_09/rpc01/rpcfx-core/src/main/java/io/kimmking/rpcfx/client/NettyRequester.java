package io.kimmking.rpcfx.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class NettyRequester {
    private static final NettyRequester INST = new NettyRequester();

    public static NettyRequester getInstance() {
        return INST;
    }

    private Bootstrap bootstrap;

    private NettyRequester() {
        EventLoopGroup group = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    public String request(String url, String body) throws URISyntaxException {
        URI uri = new URI(url);
        String host = uri.getHost();
        int port = uri.getPort();

        NettyConsumerChannelInitializer initializer = new NettyConsumerChannelInitializer();
        bootstrap.handler(initializer);
        try {
            Channel ch = bootstrap.connect(host, port).sync().channel();
            ch.writeAndFlush(makeRequest(uri, body));
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return initializer.getResponse();
    }

    private HttpRequest makeRequest(URI uri, String body) {
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.getRawPath());
        request.headers().set(HttpHeaderNames.HOST, uri.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        request.content().writeBytes(body.getBytes(StandardCharsets.UTF_8));
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        return request;
    }

}
