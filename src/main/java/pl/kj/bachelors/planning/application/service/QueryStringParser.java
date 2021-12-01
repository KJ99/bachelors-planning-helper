package pl.kj.bachelors.planning.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class QueryStringParser {
    private final ObjectMapper objectMapper;

    @Autowired
    public QueryStringParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> parse(@NonNull URI uri) {
        String query = uri.getQuery() != null ? uri.getQuery() : "";
        String[] elements = query.split("&");
        Map<String, Object> values = new HashMap<>();
        for(String element : elements) {
            if(element.contains("=")) {
                String[] keyVal = element.split("=");
                String value = keyVal.length > 1 ? keyVal[1] : "";
                values.put(keyVal[0], value);
            } else {
                values.put(element, "");
            }
        }

        return values;
    }

    public <T> T parse(@NonNull URI uri, Class<T> modelClass) {
        Map<String, Object> attributes = this.parse(uri);

        return this.objectMapper.convertValue(attributes, modelClass);
    }
}
