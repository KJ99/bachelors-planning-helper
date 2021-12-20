package pl.kj.bachelors.planning.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private String algorithm;
    private long validTimeInMinutes;
    private String type;
    private WebSocketTokenConfig socket;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public long getValidTimeInMinutes() {
        return validTimeInMinutes;
    }

    public void setValidTimeInMinutes(long validTimeInMinutes) {
        this.validTimeInMinutes = validTimeInMinutes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WebSocketTokenConfig getSocket() {
        return socket;
    }

    public void setSocket(WebSocketTokenConfig socket) {
        this.socket = socket;
    }
}
