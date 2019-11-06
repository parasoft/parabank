package com.parasoft.parabank.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

/**
 * Utility methods for Spring MVC
 */
public final class ViewUtil {
    private ViewUtil() { }

    /**
     * Create an error view with error message
     *
     * @param errorMessage the error message resource bundle key to display
     * @return Spring MVC view result
     */
    public static ModelAndView createErrorView(String errorMessage) {
        return createErrorView(errorMessage, null);
    }

    /**
     * Create an error view with error message
     *
     * @param errorMessage the error message resource bundle key to display
     * @param parameters parameters associated with the resource bundle message
     * @return Spring MVC view result
     */
    public static ModelAndView createErrorView(String errorMessage, Object[] parameters) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", errorMessage);
        if (parameters != null && parameters.length > 0) {
            model.put("parameters", parameters);
        }
        return new ModelAndView("error", "model", model);
    }
}
