package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.github.qszhu.gateway.upstream.host.UpstreamHostsProvider;

import java.util.List;
import java.util.Random;

public class WeightedChooser implements UpstreamChooser {
    private final UpstreamHostsProvider provider;

    public WeightedChooser(UpstreamHostsProvider provider) {
        this.provider = provider;
    }

    @Override
    public UpstreamHost choose() {
        List<UpstreamHost> hosts = provider.getHosts();
        Random r = new Random();
        int total = 0;
        for (UpstreamHost h : hosts) {
            total += h.weight;
        }
        int idx = r.nextInt(total);
        total = 0;
        for (UpstreamHost h : hosts) {
            if (idx >= total && idx < total + h.weight) return h;
            total += h.weight;
        }
        return null;
    }
}
