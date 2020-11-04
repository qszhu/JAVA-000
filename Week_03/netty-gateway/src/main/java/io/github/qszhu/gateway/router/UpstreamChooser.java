package io.github.qszhu.gateway.router;

import io.github.qszhu.gateway.upstream.host.UpstreamHost;

public interface UpstreamChooser {
    UpstreamHost choose();
}
