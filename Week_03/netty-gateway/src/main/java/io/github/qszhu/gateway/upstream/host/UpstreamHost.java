package io.github.qszhu.gateway.upstream.host;

public class UpstreamHost {
    public final String url;
    public final int weight;

    public UpstreamHost(String url, int weight) {
        this.url = url;
        this.weight = weight;
    }
}
