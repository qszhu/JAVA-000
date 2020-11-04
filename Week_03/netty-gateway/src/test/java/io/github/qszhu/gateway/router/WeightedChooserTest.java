package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.HardCodeUpstreamHostProvider;
import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.github.qszhu.gateway.upstream.host.UpstreamHostsProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightedChooserTest {
    private UpstreamHostsProvider provider;
    private UpstreamChooser chooser;

    @Before
    public void setUp() {
        provider = new HardCodeUpstreamHostProvider();
        chooser = new WeightedChooser(provider);
    }

    @Test
    public void testWeighted() {
        List<UpstreamHost> hosts = provider.getHosts();

        List<Integer> counts = new ArrayList<>();
        Map<String, Integer> url2Idx = new HashMap<>();

        for (int i = 0; i < hosts.size(); i++) {
            counts.add(0);
            url2Idx.put(hosts.get(i).url, i);
        }

        final int ITER = 100000;
        final double THRES = 0.01;

        for (int i = 0; i < ITER; i++) {
            UpstreamHost host = chooser.choose();
            int idx = url2Idx.get(host.url);
            counts.set(idx, counts.get(idx) + 1);
        }

        for (int i = 1; i < counts.size(); i++) {
            double r1 = counts.get(i - 1) / (double)counts.get(i);
            double r2 = hosts.get(i - 1).weight / (double)hosts.get(i).weight;
            Assert.assertTrue(Math.abs(r1 - r2) < THRES);
        }
    }
}
