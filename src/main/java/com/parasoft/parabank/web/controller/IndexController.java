package com.parasoft.parabank.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.domain.logic.NewsManager;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.CustomRamlGenerator;

/**
 * Controller for home page
 */
@Controller("/index.htm")
@RequestMapping("/index.htm")
public class IndexController extends AbstractBankController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Resource(name = "newsManager")
    private NewsManager newsManager;

    @Resource(name = "customRamlGenerator")
    private CustomRamlGenerator customRamlGenerator;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the customRamlGenerator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 28, 2015</DD>
     * </DL>
     *
     * @return the value of customRamlGenerator field
     */
    public CustomRamlGenerator getCustomRamlGenerator() {
        return customRamlGenerator;
    }

    @RequestMapping
    public ModelAndView handleRequest(final HttpSession session) throws Exception {
        final Map<String, Object> model = new HashMap<>();
        ModelAndView mav;
        try {

            //          if (request.getSession().getAttribute("ConnType") == null) {
            //          request.getSession().setAttribute("ConnType", "JDBC");
            //      }
            if (session.getAttribute("ConnType") == null) {
                session.setAttribute("ConnType", "JDBC");
            }

            final Map<Date, List<News>> news = newsManager.getLatestNews();

            if (news.isEmpty()) {
                //response.sendRedirect("initializeDB.htm");
                log.warn("Database not yet initialized. Initializing...");
                mav = new ModelAndView(new RedirectView("/initializeDB.htm", true));
            } else {
                for (final Entry<Date, List<News>> newsDate : news.entrySet()) {
                    model.put("date", newsDate.getKey());
                    model.put(Constants.NEWS, newsDate.getValue());
                }
                mav = new ModelAndView(Constants.INDEX, "model", model);
            }
        } catch (final DataAccessException e) {
            log.warn("Database not yet initialized. Initializing...");
            //response.sendRedirect("initializeDB.htm");
            //return null;
            mav = new ModelAndView(new RedirectView("/initializeDB.htm", true));
        }
        //adding the RAML generation here
        //getCustomRamlGenerator().generateRaml();
        return mav;
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the customRamlGenerator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 28, 2015</DD>
     * </DL>
     *
     * @param aCustomRamlGenerator
     *            new value for the customRamlGenerator property
     */
    public void setCustomRamlGenerator(final CustomRamlGenerator aCustomRamlGenerator) {
        customRamlGenerator = aCustomRamlGenerator;
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.INDEX)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    public void setNewsManager(final NewsManager newsManager) {
        this.newsManager = newsManager;
    }
    //    @RequestMapping("/index.htm")
    //    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
    //            throws Exception {
    //        final Map<String, Object> model = new HashMap<String, Object>();
    //        ModelAndView mav;
    //        try {
    //
    //            if (request.getSession().getAttribute("ConnType") == null) {
    //                request.getSession().setAttribute("ConnType", "JDBC");
    //            }
    //            final Map<Date, List<News>> news = newsManager.getLatestNews();
    //
    //            if (news.isEmpty()) {
    //                //response.sendRedirect("initializeDB.htm");
    //                log.warn("Database not yet initialized. Initializing...");
    //                mav =  new ModelAndView(new RedirectView("/initializeDB.htm", true));
    //            } else {
    //                for (final Entry<Date, List<News>> newsDate : news.entrySet()) {
    //                    model.put("date", newsDate.getKey());
    //                    model.put(Constants.NEWS, newsDate.getValue());
    //                }
    //                mav = new ModelAndView(Constants.INDEX, "model", model);
    //            }
    //        } catch (final DataAccessException e) {
    //            log.warn("Database not yet initialized. Initializing...");
    //            //response.sendRedirect("initializeDB.htm");
    //            //return null;
    //            mav =  new ModelAndView(new RedirectView("/initializeDB.htm", true));
    //        }
    //
    //        return mav;
    //    }

}
