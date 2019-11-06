package com.parasoft.parabank.web;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Override default view resolver behavior to forward to template page
 */
public class TemplateViewResolver extends InternalResourceViewResolver implements ViewResolver {
    static final String VIEW_ATTRIBUTE = "view";
    static final String TEMPLATE_VIEW_NAME = "template";

    @Override
    public View resolveViewName(String viewName, Locale locale)
            throws Exception {
        getAttributesMap().put(VIEW_ATTRIBUTE, viewName);
        return super.resolveViewName(TEMPLATE_VIEW_NAME, locale);
    }

    @Override
    public boolean isCache() {
        return false;
    }
}
