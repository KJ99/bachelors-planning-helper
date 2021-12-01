package pl.kj.bachelors.planning.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.socket")
public class WebSocketTokenConfig {
    private String secret;
    private int validTimeInMinutes;

    public int getValidTimeInMinutes() {
        return validTimeInMinutes;
    }

    public void setValidTimeInMinutes(int validTimeInMinutes) {
        this.validTimeInMinutes = validTimeInMinutes;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
