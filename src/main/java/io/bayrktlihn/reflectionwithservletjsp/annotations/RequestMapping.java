package io.bayrktlihn.reflectionwithservletjsp.annotations;

import io.bayrktlihn.reflectionwithservletjsp.enums.RequestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface RequestMapping {

    String path() default "";

    RequestMethod method() default RequestMethod.GET;


}
