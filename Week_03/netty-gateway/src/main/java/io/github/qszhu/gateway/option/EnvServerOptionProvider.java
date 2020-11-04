package io.github.qszhu.gateway.option;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.util.internal.StringUtil;

public class EnvServerOptionProvider implements ServerOptionProvider {
    private final String ENV_SERVER_SO_BACKLOG = "SERVER_SO_BACKLOG";
    private final String ENV_SERVER_TCP_NODELAY = "SERVER_TCP_NODELAY";
    private final String ENV_SERVER_SO_KEEPALIVE = "SERVER_SO_KEEPALIVE";
    private final String ENV_SERVER_SO_REUSEADDR = "SERVER_SO_REUSEADDR";
    private final String ENV_SERVER_SO_RCVBUF = "SERVER_SO_RCVBUF";
    private final String ENV_SERVER_SO_SNDBUF = "SERVER_SO_SNDBUF";
    private final String ENV_SERVER_SO_REUSEPORT = "SERVER_SO_REUSEPORT";
    private final String ENV_CLIENT_SO_KEEPALIVE = "CLIENT_SO_KEEPALIVE";

    @Override
    public void setup(ServerBootstrap b) {
        String t;

        t = System.getenv(ENV_SERVER_SO_BACKLOG);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.option(ChannelOption.SO_BACKLOG, Integer.parseInt(t));
        }

        t = System.getenv(ENV_SERVER_TCP_NODELAY);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.option(ChannelOption.TCP_NODELAY, Boolean.parseBoolean(t));
        }

        t = System.getenv(ENV_SERVER_SO_KEEPALIVE);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.option(ChannelOption.SO_KEEPALIVE, Boolean.parseBoolean(t));
        }

        t = System.getenv(ENV_SERVER_SO_REUSEADDR);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.option(ChannelOption.SO_REUSEADDR, Boolean.parseBoolean(t));
        }

        t = System.getenv(ENV_SERVER_SO_RCVBUF);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.option(ChannelOption.SO_RCVBUF, Integer.parseInt(t));
        }

        t = System.getenv(ENV_SERVER_SO_SNDBUF);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.option(ChannelOption.SO_SNDBUF, Integer.parseInt(t));
        }

        t = System.getenv(ENV_SERVER_SO_REUSEPORT);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.option(EpollChannelOption.SO_REUSEPORT, Boolean.parseBoolean(t));
        }

        t = System.getenv(ENV_CLIENT_SO_KEEPALIVE);
        if (!StringUtil.isNullOrEmpty(t)) {
            b.childOption(ChannelOption.SO_KEEPALIVE, Boolean.parseBoolean(t));
        }
    }
}
