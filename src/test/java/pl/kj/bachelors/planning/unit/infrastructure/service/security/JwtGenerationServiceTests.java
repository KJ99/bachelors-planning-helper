package pl.kj.bachelors.planning.unit.infrastructure.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.config.JwtConfig;
import pl.kj.bachelors.planning.domain.config.WebSocketTokenConfig;
import pl.kj.bachelors.planning.infrastructure.service.security.JwtGenerationService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtGenerationServiceTests extends BaseUnitTest {
    @Autowired
    private JwtGenerationService service;
    @Autowired
    private JwtConfig tokenConfig;

    @Test
    public void testGenerateWebSocketAccessToken() {
        String userId = "uid-1";
        String claimData = "claim";
        Map<String, Object> claims = new HashMap<>();
        claims.put("claim", claimData);

        String token = this.service.generateWebSocketAccessToken(userId, claims);

        assertThat(token).isNotEmpty();

        Claims resultClaims = this.parseClaims(token);
        assertThat(resultClaims).isNotNull();
        assertThat(resultClaims.getSubject()).isEqualTo(userId);
        assertThat(resultClaims.get("claim")).isEqualTo(claimData);
    }

    private Claims parseClaims(String jwt) {
        SecretKeySpec spec = new SecretKeySpec(
                this.tokenConfig.getSocket().getSecret().getBytes(),
                this.tokenConfig.getAlgorithm()
        );
        DefaultJwtParser parser = new DefaultJwtParser();
        parser.setSigningKey(spec);

        return parser.parseClaimsJws(jwt).getBody();
    }
}
