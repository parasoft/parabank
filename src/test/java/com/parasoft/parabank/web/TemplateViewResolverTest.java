package com.parasoft.parabank.web;

import static org.junit.Assert.*;

import java.util.*;

import javax.annotation.*;

import org.junit.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.*;

import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-6
 *
 */
public class TemplateViewResolverTest extends AbstractParaBankTest {
    @Resource(name = "viewResolver")
    private ViewResolver viewResolver;

    public void setViewResolver(final ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    @Test
    public void testResolveViewName() throws Exception {
        final View view = viewResolver.resolveViewName("test", Locale.getDefault());
        assertTrue(view instanceof AbstractView);
        final AbstractView abstractView = (AbstractView) view;
        assertEquals(TemplateViewResolver.TEMPLATE_VIEW_NAME, abstractView.getBeanName());
        final Map<String, Object> attributes = abstractView.getAttributesMap();
        assertEquals("test", attributes.get(TemplateViewResolver.VIEW_ATTRIBUTE));
    }
}
