package pl.kj.bachelors.planning.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.kj.bachelors.planning.application.security.AuthenticationHandlerInterceptor;

@Component
public class ApplicationConfigurer implements WebMvcConfigurer {
    private final AuthenticationHandlerInterceptor authInterceptor;

    @Autowired
    public ApplicationConfigurer(AuthenticationHandlerInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(this.authInterceptor);
    }
}
