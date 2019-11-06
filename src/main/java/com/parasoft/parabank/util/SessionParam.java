package com.parasoft.parabank.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>Annotation used to pass Session arguments
 * <p>
 * This was taken whole cloth from the
 * <a href="https://karthikg.wordpress.com/2009/11/08/learn-to-customize-spring-mvc-controller-method-arguments/" >post
 * </a> on the <a href="https://karthikg.wordpress.com/" >Karthik's Weblog </a>. This provides the same facility for
 * Session attributes as the {@link RequestParam} provides for the Request parameters. This alleviates the necessity of
 * either passing a HttpRequest or HttpSession objects into the annotated POJO's preserving the 'Plain' aspect of your
 * annotated Controllers</DD>
 * <DT>Date:</DT>
 * <DD>Oct 13, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionParam {

    //    /**
    //     * The default value to use as a fall-back. Supplying a default value implicitly sets {@link #required()} to false.
    //     */
    //    String defaultValue() default "";

    //    * Alternatively, provide a {@link #defaultValue() defaultValue}, which implicitly sets this flag to false.
    /**
     * Whether the parameter is required. Default is true, leading to an exception thrown in case of the parameter
     * missing in the request. Switch this to false if you prefer a null in case of the parameter missing.
     */
    boolean required() default true;

    /**
     * The name of the Session attribute to bind to.
     */
    String value() default "";

}
