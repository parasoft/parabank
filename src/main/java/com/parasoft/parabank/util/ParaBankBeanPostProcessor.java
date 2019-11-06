package com.parasoft.parabank.util;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>A bean post processor that will insure that the request processing is synchronized on session so that there is
 * possibility of a conflict across multiple requests on the same session</DD>
 * <DT>Date:</DT>
 * <DD>Oct 16, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
@Component("parabankBeanPostProcessor")
public class ParaBankBeanPostProcessor implements BeanPostProcessor {
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParaBankBeanPostProcessor.class);

    //@Resource(name = "sessionArgResolver")
    private SessionParamArgumentResolver resolver;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add forceResolver description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param resolvers
     */
    protected void forceResolver(final List<HandlerMethodArgumentResolver> resolvers) {
        boolean found = false;
        for (final HandlerMethodArgumentResolver hmar : resolvers) {
            if (hmar.getClass().isAssignableFrom(getResolver().getClass())) {
                found = true;
                break;
            }
        }
        if (!found) {
            resolvers.add(getResolver());
            log.debug("added resolver");
        } else {
            log.debug("resolver already exists");
        }
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the resolver property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @return the value of resolver field
     */
    public SessionParamArgumentResolver getResolver() {
        if (resolver == null) {
            resolver = new SessionParamArgumentResolver();
        }
        return resolver;
    }

    @Override
    public Object postProcessAfterInitialization(final Object aParamObject, final String aParamString)
            throws BeansException {
        log.trace("ParaBankBeanPostProcessor got {}", aParamString);

        if (aParamObject instanceof RequestMappingHandlerAdapter) {
            final RequestMappingHandlerAdapter rmha = (RequestMappingHandlerAdapter) aParamObject;
            rmha.setSynchronizeOnSession(true);
            log.debug("Synchronization on session forced");
            //            List<HandlerMethodArgumentResolver> resolvers = rmha.getCustomArgumentResolvers();
            //            if (resolvers == null) {
            //                rmha.setCustomArgumentResolvers(new ArrayList<HandlerMethodArgumentResolver>());
            //                resolvers = rmha.getCustomArgumentResolvers();
            //            }
            //            forceResolver(resolvers);
        }

        return aParamObject;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object aParamObject, final String aParamString)
            throws BeansException {
        return aParamObject;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the resolver property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param aResolver
     *            new value for the resolver property
     */
    public void setResolver(final SessionParamArgumentResolver aResolver) {
        resolver = aResolver;
    }

}
