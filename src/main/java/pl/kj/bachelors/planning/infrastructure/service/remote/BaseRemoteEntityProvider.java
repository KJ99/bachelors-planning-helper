package pl.kj.bachelors.planning.infrastructure.service.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.kj.bachelors.planning.domain.config.JwtConfig;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.Optional;

public abstract class BaseRemoteEntityProvider<T, ID> {
    protected final JwtConfig jwtConfig;
    protected final ObjectMapper objectMapper;

    protected BaseRemoteEntityProvider(JwtConfig jwtConfig, ObjectMapper objectMapper) {
        this.jwtConfig = jwtConfig;
        this.objectMapper = objectMapper;
    }

    protected abstract String getUrl(ID entityId);
    protected abstract Class<T> getModelClass();

    public Optional<T> get(ID entityId) {
        var restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>("parameters", this.getHeaders());
        String url = this.getUrl(entityId);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        T result;
        if(response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() <= 299) {
            try {
                result = this.objectMapper.readValue(response.getBody(), this.getModelClass());
            } catch (JsonProcessingException e) {
                result = null;
            }
        } else {
            result = null;
        }

        return Optional.ofNullable(result);
    }

    protected HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        if(this.shouldPassAuthorizationHeader()) {
            headers.add(HttpHeaders.AUTHORIZATION, this.createAuthorization());
        }

        if(this.shouldPassCacheControl()) {
            headers.add(HttpHeaders.CACHE_CONTROL, RequestHandler.getHeaderValue(HttpHeaders.CACHE_CONTROL).orElse(""));
        }

        return headers;
    }

    protected String createAuthorization() {
        String[] headerValueParts =  RequestHandler
                .getHeaderValue(HttpHeaders.AUTHORIZATION)
                .orElse("")
                .split(" ");

        String token = "";
        if(headerValueParts.length > 1 && headerValueParts[0].equals(this.jwtConfig.getType())) {
            token = headerValueParts[1];
        }

        return String.format(
                "%s %s",
                this.jwtConfig.getType(),
                token
        );
    }

    protected boolean shouldPassAuthorizationHeader() {
        return true;
    }

    protected boolean shouldPassCacheControl() {
        return false;
    }
}
