package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.github.qszhu.gateway.upstream.host.UpstreamHostsProvider;

import java.util.List;
import java.util.Random;

public class RandomChooser implements UpstreamChooser {
    private final UpstreamHostsProvider provider;

    public RandomChooser(UpstreamHostsProvider provider) {
        this.provider = provider;
    }

    @Override
    public UpstreamHost choose() {
        List<UpstreamHost> hosts = provider.getHosts();
        Random r = new Random();
        int idx = r.nextInt(hosts.size());
        return hosts.get(idx);
    }
}
