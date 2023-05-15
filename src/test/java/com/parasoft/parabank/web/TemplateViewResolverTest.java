package com.parasoft.parabank.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;

import com.parasoft.parabank.test.util.AbstractParaBankTest;

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

    /**
     * @req PAR-10
     * @throws Exception
     */
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
