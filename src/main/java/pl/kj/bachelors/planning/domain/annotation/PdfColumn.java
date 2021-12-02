package pl.kj.bachelors.planning.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface PdfColumn {
    String name() default "";
    float relativeWidth() default  1f;
    int order() default 0;
}
