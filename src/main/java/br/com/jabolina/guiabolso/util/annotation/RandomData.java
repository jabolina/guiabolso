package br.com.jabolina.guiabolso.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RandomData {

    int max() default Integer.MAX_VALUE;

    int min() default Integer.MIN_VALUE;

    Type type() default Type.TEXT;

    /**
     * Defines wich type of pattern should be used.
     */
    enum Type {
        /**
         * When the type is {@link #TEXT}, then the {@link #max()} method should
         * be used.
         */
        TEXT,

        /**
         * When the type is {@link #NUMBER}, then the fields {@link #min()} and {@link #max()}
         * should be used.
         */
        NUMBER,

        /**
         * When the type is date, then no method above will be used. By the default will be created
         * dates as old as 3 years in the past, with limit to the current date.
         */
        DATE,
    }
}
