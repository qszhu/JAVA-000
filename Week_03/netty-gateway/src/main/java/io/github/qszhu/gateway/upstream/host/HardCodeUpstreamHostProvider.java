package io.github.qszhu.gateway.upstream.host;

import java.util.Arrays;
import java.util.List;

public class HardCodeUpstreamHostProvider implements UpstreamHostsProvider {
    @Override
    public List<UpstreamHost> getHosts() {
        return Arrays.asList(
                new UpstreamHost("http://localhost:8801", 3),
                new UpstreamHost("http://localhost:8802", 11),
                new UpstreamHost("http://localhost:8803", 23)
        );
    }
}
