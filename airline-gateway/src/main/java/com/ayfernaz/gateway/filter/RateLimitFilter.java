package com.ayfernaz.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends AbstractGatewayFilterFactory<RateLimitFilter.Config> {

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private LocalDate currentDate = LocalDate.now();

    public RateLimitFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            resetIfNewDay();

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();

            if (!path.startsWith("/api/v1/flights/search")) {
                return chain.filter(exchange);
            }

            String clientIp = request.getRemoteAddress() != null
                    ? request.getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";

            String key = clientIp + ":" + path;
            int count = requestCounts.getOrDefault(key, 0);

            if (count >= config.getLimit()) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }

            requestCounts.put(key, count + 1);
            return chain.filter(exchange);
        };
    }

    private void resetIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!today.equals(currentDate)) {
            requestCounts.clear();
            currentDate = today;
        }
    }

    public static class Config {
        private int limit = 3;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }
}