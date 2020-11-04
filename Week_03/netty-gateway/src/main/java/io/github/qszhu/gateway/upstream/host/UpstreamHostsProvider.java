package io.github.qszhu.gateway.upstream.host;

import java.util.List;

public interface UpstreamHostsProvider {
    List<UpstreamHost> getHosts();
}
