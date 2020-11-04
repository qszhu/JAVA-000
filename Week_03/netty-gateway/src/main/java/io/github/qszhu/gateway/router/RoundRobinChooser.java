package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.github.qszhu.gateway.upstream.host.UpstreamHostsProvider;

import java.util.List;

public class RoundRobinChooser implements UpstreamChooser {
    private final UpstreamHostsProvider provider;
    private int idx = 0;

    public RoundRobinChooser(UpstreamHostsProvider provider) {
        this.provider = provider;
    }

    @Override
    public UpstreamHost choose() {
        List<UpstreamHost> hosts = provider.getHosts();
        UpstreamHost res = hosts.get(idx);
        idx = (idx + 1) % hosts.size();
        return res;
    }
}
