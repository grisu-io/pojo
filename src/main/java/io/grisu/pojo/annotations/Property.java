package io.grisu.pojo.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Property {
   String name();
   boolean serializer() default false;
   Class serializerClass() default String.class;
   String value() default "";
}
