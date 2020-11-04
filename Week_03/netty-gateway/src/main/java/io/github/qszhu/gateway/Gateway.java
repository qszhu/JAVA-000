package io.github.qszhu.gateway;

import io.github.qszhu.gateway.filter.AddMyHeaderRequestFilter;
import io.github.qszhu.gateway.filter.GatewayRequestFilters;
import io.github.qszhu.gateway.option.EnvServerOptionProvider;
import io.github.qszhu.gateway.option.ServerOptionProvider;
import io.github.qszhu.gateway.router.GatewayRouter;
import io.github.qszhu.gateway.router.RandomChooser;
import io.github.qszhu.gateway.upstream.host.EnvUpstreamHostProvider;
import io.github.qszhu.gateway.upstream.host.UpstreamHostsProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Gateway {
    private static final String ENV_HOST = "HOST";
    private static final String ENV_PORT = "PORT";

    public static void main(String[] args) throws InterruptedException {
        String host = System.getenv(ENV_HOST);
        int port = Integer.parseInt(System.getenv(ENV_PORT));

        // request filters
        GatewayRequestFilters.getInstance().addFilter(new AddMyHeaderRequestFilter());

        // router
        UpstreamHostsProvider provider = new EnvUpstreamHostProvider();
        GatewayRouter.getInstance().setChooser(new RandomChooser(provider));

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();

            ServerOptionProvider p = new EnvServerOptionProvider();
            p.setup(b);

            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(new GatewayInitializer());

            Channel ch = b.bind(host, port).sync().channel();
            System.out.printf("Listening on %s:%d...\n", host, port);

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
