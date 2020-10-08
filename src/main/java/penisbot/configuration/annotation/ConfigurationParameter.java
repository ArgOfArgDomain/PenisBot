package penisbot.configuration.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigurationParameter {
    String environmentKey();
    boolean sensitive() default false;
}
