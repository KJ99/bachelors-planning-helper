package pl.kj.bachelors.planning.domain.service.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import pl.kj.bachelors.planning.domain.exception.JwtInvalidException;

public interface JwtParser {
    void validateToken(String jwt) throws JwtInvalidException, ExpiredJwtException;
    String getUid(String jwt);
    Claims parseClaims(String jwt);
}
