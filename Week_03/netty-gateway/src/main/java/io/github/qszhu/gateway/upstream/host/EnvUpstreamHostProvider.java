package io.github.qszhu.gateway.upstream.host;

import java.util.Arrays;
import java.util.List;

public class EnvUpstreamHostProvider implements UpstreamHostsProvider {
    private final String ENV_UPSTREAM_URL = "UPSTREAM_URL";

    @Override
    public List<UpstreamHost> getHosts() {
        String url = System.getenv(ENV_UPSTREAM_URL);
        return Arrays.asList(new UpstreamHost(url, 1));
    }
}
