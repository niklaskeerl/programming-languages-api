package com.example.programminglanguagesapi.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private volatile Health health = Health.down().build();

    @Override
    public Health health() {
        return health;
    }

    public void setHealthUp() {
        this.health = Health.up().build();
    }
}
