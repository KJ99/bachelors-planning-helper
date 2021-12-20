package pl.kj.bachelors.planning.infrastructure.user;

import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import pl.kj.bachelors.planning.domain.model.extension.RequestAttributeName;

import java.util.Optional;
import java.util.TimeZone;

public class RequestHandler {
    public static Optional<String> getCurrentUserId() {
        return getAttribute(RequestAttributeName.USER_ID.value, String.class);
    }

    public static Optional<String> getHeaderValue(String name) {
        String value = null;
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            value = ((ServletRequestAttributes) attributes).getRequest().getHeader(name);
        }

        return Optional.ofNullable(value);
    }

    public static Optional<TimeZone> getRequestTimeZone() {
        TimeZone value = null;
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            var request = ((ServletRequestAttributes) attributes).getRequest();
            value = RequestContextUtils.getTimeZone(request);
        }

        return Optional.ofNullable(value);
    }

    public static <T> Optional<T> getAttribute(String key, Class<T> destClass) {
        return getAttributeFromRequest(key).or(() -> getAttributeFromWebSocketContext(key)).map(destClass::cast);
    }

    private static Optional<Object> getAttributeFromRequest(String key) {
        Object value = null;
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null && attributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST) != null) {
            value = attributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST);
        }

        return Optional.ofNullable(value);
    }

    private static Optional<Object> getAttributeFromWebSocketContext(String key) {
        SimpAttributes attributes = SimpAttributesContextHolder.currentAttributes();

        return Optional.ofNullable(attributes.getAttribute(key));
    }
}
