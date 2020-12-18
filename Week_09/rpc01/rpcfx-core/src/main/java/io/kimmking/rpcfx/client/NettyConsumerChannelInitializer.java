package io.kimmking.rpcfx.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class NettyConsumerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private NettyConsumerHandler consumerHandler;

    public NettyConsumerChannelInitializer() {
        consumerHandler = new NettyConsumerHandler();
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(1048576));
        pipeline.addLast(consumerHandler);

    }

    public String getResponse() {
        return consumerHandler.getResponse();
    }
}
