package com.parasoft.parabank.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.parasoft.parabank.util.Constants;

/**
 * Spring MVC Interceptor that tests if a user is logged in (i.e. session object exists) before granting access to
 * protected pages
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, Constants.USERSESSION);
        if (userSession == null) {
            final String url = request.getServletPath();
            final String query = request.getQueryString();
            final ModelAndView modelAndView = new ModelAndView(Constants.LOGINFORM);
            if (query != null) {
                log.warn("User is not logged in and attempting to access protected page: " + url + "?" + query);
                modelAndView.addObject("loginForwardAction", url + "?" + query);
            } else {
                log.warn("User is not logged in and attempting to access protected page: " + url);
                modelAndView.addObject("loginForwardAction", url);
            }
            throw new ModelAndViewDefiningException(modelAndView);
        }
        return super.preHandle(request, response, handler);
    }
}
