package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.HardCodeUpstreamHostProvider;
import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.github.qszhu.gateway.upstream.host.UpstreamHostsProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class RandomChooserTest {
    private UpstreamHostsProvider provider;
    private UpstreamChooser chooser;

    @Before
    public void setUp() {
        provider = new HardCodeUpstreamHostProvider();
        chooser = new RandomChooser(provider);
    }

    @Test
    public void testRandom() {
        List<UpstreamHost> hosts = provider.getHosts();

        List<Integer> counts = new ArrayList<>();
        Map<String, Integer> url2Idx = new HashMap<>();

        for (int i = 0; i < hosts.size(); i++) {
            counts.add(0);
            url2Idx.put(hosts.get(i).url, i);
        }

        final int ITER = 100000;
        final int THRES = ITER / 100;

        for (int i = 0; i < ITER; i++) {
            UpstreamHost host = chooser.choose();
            int idx = url2Idx.get(host.url);
            counts.set(idx, counts.get(idx) + 1);
        }

        for (int i = 1; i < counts.size(); i++) {
            Assert.assertTrue(Math.abs(counts.get(i) - counts.get(i - 1)) <= THRES);
        }
    }
}
