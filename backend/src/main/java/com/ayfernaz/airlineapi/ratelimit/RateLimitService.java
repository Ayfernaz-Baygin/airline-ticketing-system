package com.ayfernaz.airlineapi.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitService {

    private final Map<String, Integer> requestCounts = new HashMap<>();
    private LocalDate currentDate = LocalDate.now();

    @Value("${rate.limit.enabled:true}")
    private boolean rateLimitEnabled;

    @Value("${rate.limit.max-requests-per-day:3}")
    private int maxRequestsPerDay;

    public void checkLimit(String clientKey) {
        if (!rateLimitEnabled) {
            return;
        }

        resetIfNewDay();

        int count = requestCounts.getOrDefault(clientKey, 0);

        if (count >= maxRequestsPerDay) {
            throw new RateLimitException(
                "Daily search limit exceeded (max " + maxRequestsPerDay + " requests)"
            );
        }

        requestCounts.put(clientKey, count + 1);
    }

    private void resetIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!today.equals(currentDate)) {
            requestCounts.clear();
            currentDate = today;
        }
    }
}