package pl.kj.bachelors.planning.application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import pl.kj.bachelors.planning.application.dto.request.ConnectWebSocketQuery;
import pl.kj.bachelors.planning.application.service.QueryStringParser;
import pl.kj.bachelors.planning.domain.config.WebSocketTokenConfig;
import pl.kj.bachelors.planning.domain.exception.JwtInvalidException;
import pl.kj.bachelors.planning.domain.model.extension.RequestAttributeName;
import pl.kj.bachelors.planning.domain.service.security.JwtParser;

import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final QueryStringParser queryStringParser;
    private final WebSocketTokenConfig config;
    private final JwtParser tokenParser;

    @Autowired
    public WebSocketHandshakeInterceptor(
            QueryStringParser queryStringParser,
            WebSocketTokenConfig config,
            JwtParser tokenParser) {
        this.queryStringParser = queryStringParser;
        this.config = config;
        this.tokenParser = tokenParser;
    }

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler webSocketHandler,
            @NonNull Map<String, Object> attributes) {
        boolean result;
        ConnectWebSocketQuery query = this.queryStringParser.parse(request.getURI(), ConnectWebSocketQuery.class);

        try {
            this.tokenParser.validateToken(query.getKey(), this.config.getSecret());
            Claims claims = this.tokenParser.parseClaims(query.getKey(), this.config.getSecret());
            attributes.put(RequestAttributeName.USER_ID.value, claims.getSubject());
            attributes.put(RequestAttributeName.PLANNING_ID.value, claims.get("pid"));

            result = true;
        } catch (JwtInvalidException | ExpiredJwtException ex) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            result = false;
        }

        return result;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, Exception e) {

    }
}
