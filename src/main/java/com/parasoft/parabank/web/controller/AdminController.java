package com.parasoft.parabank.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.AdminForm;

/**
 * Controller for modifying ParaBank parameters, servers, and behavior
 */
@Controller("/admin.htm")
@SessionAttributes(Constants.ADMINFORM)
@RequestMapping("/admin.htm")
public class AdminController extends AbstractBaseAdminController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getFormModelAndView(final Model model) throws Exception {
        final AdminForm form = getAdminManager().populateAdminForm(new AdminForm());
        final ModelAndView mav = super.prepForm(model, form);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@Validated final @ModelAttribute(Constants.ADMINFORM) AdminForm form,
        final BindingResult errors, final Model model) throws Exception {
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }
        // final AdminForm form = getCommand(model);

        final ModelAndView modelAndView = new ModelAndView(getFormView(), errors.getModel());
        modelAndView.addAllObjects(model.asMap());
        saveAdminSettings(form);

        //        log.info("Using regular JDBC connection. AccessModeController not supported yet");
        //        for (final Entry<String, String> entry : form.getParameters().entrySet()) {
        //            getAdminManager().setParameter(entry.getKey(), entry.getValue());
        //        }

        modelAndView.addObject("message", "settings.saved");

        return modelAndView;
    }

}
