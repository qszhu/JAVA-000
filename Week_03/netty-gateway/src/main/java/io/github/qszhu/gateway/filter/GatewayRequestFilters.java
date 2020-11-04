package io.github.qszhu.gateway.filter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GatewayRequestFilters implements Iterable<GatewayRequestFilter> {
    private static final GatewayRequestFilters inst = new GatewayRequestFilters();

    private final List<GatewayRequestFilter> filters = new ArrayList<>();

    private GatewayRequestFilters() {
    }

    public static GatewayRequestFilters getInstance() {
        return inst;
    }

    public boolean addFilter(GatewayRequestFilter filter) {
        return filters.add(filter);
    }

    public boolean removeFilter(GatewayRequestFilter filter) {
        return filters.remove(filter);
    }

    @NotNull
    @Override
    public Iterator<GatewayRequestFilter> iterator() {
        return filters.iterator();
    }
}
