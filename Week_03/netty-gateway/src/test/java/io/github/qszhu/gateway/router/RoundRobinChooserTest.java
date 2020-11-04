package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.HardCodeUpstreamHostProvider;
import io.github.qszhu.gateway.upstream.host.UpstreamHost;
import io.github.qszhu.gateway.upstream.host.UpstreamHostsProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RoundRobinChooserTest {
    private UpstreamHostsProvider provider;
    private UpstreamChooser chooser;

    @Before
    public void setUp() {
        provider = new HardCodeUpstreamHostProvider();
        chooser = new RoundRobinChooser(provider);
    }

    @Test
    public void testRoundRobin() {
        List<UpstreamHost> hosts = provider.getHosts();
        for (int i = 0; i < 3; i++) {
            for (UpstreamHost host : hosts) {
                Assert.assertEquals(host.url, chooser.choose().url);
            }
        }
    }
}
