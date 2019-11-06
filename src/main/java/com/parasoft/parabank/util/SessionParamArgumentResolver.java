package com.parasoft.parabank.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>Argument resolver for SessionParam.
 * <p>
 * This was taken whole cloth from the
 * <a href="https://karthikg.wordpress.com/2009/11/08/learn-to-customize-spring-mvc-controller-method-arguments/" >post
 * </a> on the <a href="https://karthikg.wordpress.com/" >Karthik's Weblog </a>. This provides the same facility for
 * Session attributes as the {@link RequestParam} provides for the Request parameters. This alleviates the necessity of
 * either passing a HttpRequest or HttpSession objects into the annotated POJO's preserving the 'Plain' aspect of your
 * annotated Controllers
 * </p>
 * <p>
 * re-factored to use the newer API: {@link org.springframework.web.method.support.HandlerMethodArgumentResolver} since
 * the old way is deprecated
 * </p>
 * <p>
 * The following needs to be in the parabank-servlet.xml -- that's how this class is initialized
 *
 * <pre>
    &lt;mvc:annotation-driven&gt;
        &lt;mvc:argument-resolvers&gt;
            &lt;bean  class="com.parasoft.parabank.util.SessionParamArgumentResolver" lazy-init="false" /&gt;
        &lt;/mvc:argument-resolvers&gt;
    &lt;/mvc:annotation-driven&gt;
 * </pre>
 *
 * </DD>
 * <DT>Date:</DT>
 * <DD>Oct 13, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
public class SessionParamArgumentResolver implements HandlerMethodArgumentResolver {
    @SuppressWarnings("unused")
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SessionParamArgumentResolver.class);

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Lookup the parameter on the session by name return the value. Throw exception if parameter is required and
     * does not exist, or there is no valid session for the request</DD></DD>
     * <DT>Date:</DT>
     * <DD>Oct 13, 2015</DD>
     * </DL>
     *
     * @param aParameter
     * @param aWebRequest
     * @return
     * @throws IllegalStateException
     *             if the parameter is required and it does not exist on the session
     * @throws HttpSessionRequiredException
     *             if the parameter is required and there is no valid session
     */
    protected Object extractSessionParameter(final MethodParameter aParameter, final NativeWebRequest aWebRequest)
            throws IllegalStateException, HttpSessionRequiredException {
        final SessionParam sessionParam = aParameter.getParameterAnnotation(SessionParam.class);
        final Class<?> paramType = aParameter.getParameterType();
        final String paramName = sessionParam.value();
        final boolean required = sessionParam.required();
        //final String defaultValue = sessionParam.defaultValue();
        final HttpServletRequest httprequest = (HttpServletRequest) aWebRequest.getNativeRequest();
        final HttpSession session = httprequest.getSession(false);
        Object result = null;
        if (session != null) {
            result = session.getAttribute(paramName);
        }
        //        if (result == null) {
        //            result = defaultValue;
        //        }
        if (result == null && required && session == null) {
            throw new HttpSessionRequiredException("No HttpSession found for resolving parameter '" + paramName
                + "' of type [" + paramType.getName() + "]");
        }
        if (result == null && required) {
            throw new IllegalStateException(
                "Missing parameter '" + paramName + "' of type [" + paramType.getName() + "]");
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Object resolveArgument(final MethodParameter aParameter, final ModelAndViewContainer aMavContainer,
        final NativeWebRequest aWebRequest, final WebDataBinderFactory aBinderFactory) throws Exception {
        final Object result = extractSessionParameter(aParameter, aWebRequest);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsParameter(final MethodParameter aParameter) {
        return aParameter.hasParameterAnnotation(SessionParam.class);
    }

}
