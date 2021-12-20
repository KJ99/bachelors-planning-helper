package pl.kj.bachelors.planning.infrastructure.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultJwtParser;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.config.JwtConfig;
import pl.kj.bachelors.planning.domain.exception.JwtInvalidException;
import pl.kj.bachelors.planning.domain.service.security.JwtParser;

import javax.crypto.spec.SecretKeySpec;

@Service
public class ParseJwtService implements JwtParser {
    private final JwtConfig config;

    @Autowired
    public ParseJwtService(JwtConfig config) {
        this.config = config;
    }

    @Override
    public void validateToken(String jwt) throws JwtInvalidException, ExpiredJwtException {
        this.validateToken(jwt, this.config.getSecret());
    }

    @Override
    public void validateToken(String jwt, String secret) throws JwtInvalidException, ExpiredJwtException {
        if(jwt == null) {
            throw new JwtInvalidException();
        }

        String[] chunks = jwt.split("\\.");
        if(chunks.length < 3) {
            throw new JwtInvalidException();
        }

        try {
            this.checkSignature(chunks, secret);
            this.parseClaims(jwt, secret);
        } catch (SignatureException e) {
            throw new JwtInvalidException();
        }

    }

    @Override
    public String getUid(String jwt, String secret) {
        var claims = this.parseClaims(jwt);

        return claims.getSubject();
    }

    @Override
    public String getUid(String jwt) {
        return this.getUid(jwt, this.config.getSecret());
    }

    @Override
    public Claims parseClaims(String jwt) {
        return this.parseClaims(jwt, this.config.getSecret());
    }

    @Override
    public Claims parseClaims(String jwt, String secret) throws ExpiredJwtException {
        SecretKeySpec spec = new SecretKeySpec(secret.getBytes(), this.config.getAlgorithm());
        DefaultJwtParser parser = new DefaultJwtParser();
        parser.setSigningKey(spec);

        return parser.parseClaimsJws(jwt).getBody();
    }

    private void checkSignature(String[] tokenChunks, String secret) throws JwtInvalidException {
        SignatureAlgorithm algorithm = SignatureAlgorithm.forName(this.config.getAlgorithm());
        SecretKeySpec spec = new SecretKeySpec(secret.getBytes(), this.config.getAlgorithm());
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(algorithm, spec);

        String tokenWithoutSignature = String.join(".", tokenChunks[0], tokenChunks[1]);

        if(!validator.isValid(tokenWithoutSignature, tokenChunks[2])) {
            throw new JwtInvalidException();
        }
    }
}
