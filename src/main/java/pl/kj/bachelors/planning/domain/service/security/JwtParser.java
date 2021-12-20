package pl.kj.bachelors.planning.domain.service.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import pl.kj.bachelors.planning.domain.exception.JwtInvalidException;

public interface JwtParser {
    void validateToken(String jwt) throws JwtInvalidException, ExpiredJwtException;
    void validateToken(String jwt, String secret) throws JwtInvalidException, ExpiredJwtException;
    String getUid(String jwt);
    String getUid(String jwt, String secret);
    Claims parseClaims(String jwt);
    Claims parseClaims(String jwt, String secret);
}
