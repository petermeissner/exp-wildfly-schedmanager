package annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SchedAnnotation {
    String name() default "";
    String description() default "";
    boolean enabled() default true;
}
