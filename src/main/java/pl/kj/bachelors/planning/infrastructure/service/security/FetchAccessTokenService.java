package pl.kj.bachelors.planning.infrastructure.service.security;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.config.JwtConfig;
import pl.kj.bachelors.planning.domain.service.security.AccessTokenFetcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class FetchAccessTokenService implements AccessTokenFetcher {
    private final JwtConfig config;

    @Autowired
    public FetchAccessTokenService(JwtConfig config) {
        this.config = config;
    }

    @Override
    public Optional<String> getAccessTokenFromRequest(HttpServletRequest request) {
        String token = null;
        String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(headerValue != null) {
            String[] chunks = headerValue.split(" ");
            token = chunks.length > 0 && chunks[0].equals(this.config.getType()) ? chunks[1] : null;
        }
        return Optional.ofNullable(token);
    }
}
