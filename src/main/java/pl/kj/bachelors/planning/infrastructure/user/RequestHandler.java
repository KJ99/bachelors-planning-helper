package pl.kj.bachelors.planning.infrastructure.user;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Optional;
import java.util.TimeZone;

public class RequestHandler {
    public static Optional<String> getCurrentUserId() {
        String uid = null;
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null && attributes.getAttribute("uid", RequestAttributes.SCOPE_REQUEST) != null) {
            uid = (String) attributes.getAttribute("uid", RequestAttributes.SCOPE_REQUEST);
        }
        return Optional.ofNullable(uid);
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
}
