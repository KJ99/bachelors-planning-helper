package pl.kj.bachelors.planning.infrastructure.service.security;

import com.beust.jcommander.internal.Nullable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.config.JwtConfig;
import pl.kj.bachelors.planning.domain.service.security.JwtGenerator;

import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Map;

@Service
public class JwtGenerationService implements JwtGenerator {
    private final JwtConfig config;

    @Autowired
    public JwtGenerationService(JwtConfig config) {
        this.config = config;
    }

    @Override
    public String generateWebSocketAccessToken(String userId, Map<String, Object> additionalClaims) {
        Calendar issuedAt = Calendar.getInstance();
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.MINUTE, this.config.getSocket().getValidTimeInMinutes());

        return this.generateJwt(userId, issuedAt, expiresAt, additionalClaims, this.config.getSocket().getSecret());
    }

    @Override
    public String generateToken(String sub, String secret, Map<String, Object> additionalClaims) {
        return this.generateJwt(
                sub,
                Calendar.getInstance(),
                null,
                additionalClaims,
                secret
        );
    }

    private String generateJwt(
            String userId,
            Calendar issuedAt,
            @Nullable Calendar expiresAt,
            Map<String, Object> additionalClaims,
            String secret) {
        var builder = new DefaultJwtBuilder();
        builder.setHeaderParam("alg", this.config.getAlgorithm());
        builder.setHeaderParam("typ", "JWT");

        builder.setSubject(userId);
        builder.setIssuedAt(issuedAt.getTime());
        if(expiresAt != null) {
            builder.setExpiration(expiresAt.getTime());
        }

        for(Map.Entry<String, Object> entry : additionalClaims.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }

        SignatureAlgorithm algorithm = SignatureAlgorithm.forName(config.getAlgorithm());
        SecretKeySpec spec = new SecretKeySpec(secret.getBytes(), config.getAlgorithm());

        builder.signWith(algorithm, spec);

        return builder.compact();
    }
}
