package pl.kj.bachelors.planning.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "in-app-cache")
public class CacheConfig {
    private int validTimeInHours;

    public int getValidTimeInHours() {
        return validTimeInHours;
    }

    public void setValidTimeInHours(int validTimeInHours) {
        this.validTimeInHours = validTimeInHours;
    }
}
