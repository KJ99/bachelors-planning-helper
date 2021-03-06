package pl.kj.bachelors.planning.domain.service.security;

import java.util.Map;

public interface JwtGenerator {
    String generateWebSocketAccessToken(String userId, Map<String, Object> additionalClaims);
    String generateToken(String sub, String secret, Map<String, Object> additionalClaims);
}
