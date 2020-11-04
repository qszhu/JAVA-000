package io.github.qszhu.gateway.option;

import io.netty.bootstrap.ServerBootstrap;

public interface ServerOptionProvider {
    void setup(ServerBootstrap b);
}
