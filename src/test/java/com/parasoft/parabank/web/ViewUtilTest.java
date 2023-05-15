package com.parasoft.parabank.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-5
 *
 */
public class ViewUtilTest extends AbstractParaBankTest {
    private static final String ERROR_MESSAGE = "error message";

    private static final String[] PARAMS = new String[] { "param1", "param2" };

    /**
     * @req PAR-11
     * @req PAR-21
     */
    @Test
    public void testCreateErrorView() {
        ModelAndView mav = ViewUtil.createErrorView(ERROR_MESSAGE);
        assertEquals("error", mav.getViewName());
        ModelAndViewAssert.assertViewName(mav, "error");
        @SuppressWarnings("rawtypes")
        Map map = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "model", Map.class);
        assertEquals(ERROR_MESSAGE, map.get("message"));
        assertNull(map.get("parameters"));

        mav = ViewUtil.createErrorView(ERROR_MESSAGE, PARAMS);
        assertEquals("error", mav.getViewName());
        ModelAndViewAssert.assertViewName(mav, "error");
        map = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "model", Map.class);
        assertEquals(ERROR_MESSAGE, map.get("message"));
        assertEquals(PARAMS, map.get("parameters"));
    }
}
