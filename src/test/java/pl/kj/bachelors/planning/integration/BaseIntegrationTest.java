package pl.kj.bachelors.planning.integration;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import pl.kj.bachelors.planning.BaseTest;
import pl.kj.bachelors.planning.domain.config.JwtConfig;

import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;


@AutoConfigureMockMvc
public class BaseIntegrationTest extends BaseTest {
    @Autowired
    protected JwtConfig jwtConfig;

    @Autowired
    protected MockMvc mockMvc;

    protected String generateValidAccessToken(String uid) {
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.HOUR, 10);

        return this.generateAccessToken(uid, expiresAt);
    }

    protected String generateExpiredAccessToken(String uid) {
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.HOUR, -10);

        return this.generateAccessToken(uid, expiresAt);
    }

    private String generateAccessToken(String uid, Calendar expiresAt) {
        SecretKeySpec spec = new SecretKeySpec(this.jwtConfig.getSecret().getBytes(), this.jwtConfig.getAlgorithm());

        DefaultJwtBuilder builder = new DefaultJwtBuilder();
        builder.setSubject(uid);
        builder.setExpiration(expiresAt.getTime());
        builder.signWith(SignatureAlgorithm.forName(this.jwtConfig.getAlgorithm()), spec);

        return builder.compact();
    }
}
