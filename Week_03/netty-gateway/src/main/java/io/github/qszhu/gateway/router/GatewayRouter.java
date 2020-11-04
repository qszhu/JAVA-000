package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.UpstreamHost;

public class GatewayRouter {
    private static final GatewayRouter inst = new GatewayRouter();

    public static final GatewayRouter getInstance() {
        return inst;
    }

    private GatewayRouter() {
    }

    private UpstreamChooser chooser;

    public void setChooser(UpstreamChooser chooser) {
        this.chooser = chooser;
    }

    public UpstreamHost choose() {
        return chooser.choose();
    }

}
