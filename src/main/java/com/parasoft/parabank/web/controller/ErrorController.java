package com.parasoft.parabank.web.controller;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import org.apache.cxf.interceptor.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.util.*;

import com.parasoft.parabank.util.*;

/**
 * Controller for home page
 */
@Controller("/error.htm")
@RequestMapping("/error.htm")
public class ErrorController extends AbstractBankController {
    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final Map<String, Object> model = new HashMap<String, Object>();
        final Object obj = request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE);
        if (Util.equals(request.getAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE), new Integer(404))) {
            log.warn("Page not found: " + request.getAttribute(WebUtils.ERROR_REQUEST_URI_ATTRIBUTE));
            model.put("message", "error.not.found");
            model.put("parameters", new Object[] { request.getAttribute(WebUtils.ERROR_REQUEST_URI_ATTRIBUTE) });
        } else if (obj instanceof Fault) {
            return handleFault((Fault) obj, response);
        } else if (obj instanceof Throwable && ((Throwable) obj).getCause() instanceof Fault) {
            return handleFault((Fault) ((Throwable) obj).getCause(), response);
        } else {
            if (obj instanceof Throwable) {
                log.error("Caught Error: {} : {}", obj.getClass().getName(), ((Throwable) obj).getMessage(), obj);
            }
            model.put("message", "error.internal");
        }
        return new ModelAndView("error", "model", model);
    }

    private static ModelAndView handleFault(final Fault fault, final HttpServletResponse response)
            throws IOException {
        response.setStatus(400);
        response.setContentType("text/plain");
        response.getWriter().write(fault.getLocalizedMessage());
        return null;
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // nothing to do
    }
}
