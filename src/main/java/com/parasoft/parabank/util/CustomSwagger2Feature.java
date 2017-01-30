package com.parasoft.parabank.util;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>This is necessary due to the fact that the org.apache.cxf.jaxrs.swagger.AbstractSwaggerFeature forces values,
 * that override the @Info annotation from the @SwaggerDefinition in an invalid way. if this type of override is desired
 * then a custom BeanConfig/Swagger2Feature will be needed to fix this look at the updateInfoFromConfig it wrecks the
 * Info really badly due to forced defaults from AbstractSwaggerFeature.
 * <OL>
 * <LI>Contact information is destroyed (the name gets replaced by an email "users@cxf.apache.org").</LI>
 * <LI>License becomes Apache 2.0.</LI>
 * <LI>Description if not expressly set on the bean, gets FUBARed to "The Application"</LI>
 * <LI>Version becomes 1.0.0</LI>
 * </OL>
 * </DD>
 * <DT>Date:</DT>
 * <DD>Oct 24, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
public class CustomSwagger2Feature extends Swagger2Feature {
    @SuppressWarnings("unused")
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomSwagger2Feature.class);

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>CustomSwagger2Feature Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     */
    public CustomSwagger2Feature() {
        super();
        setTitle(null);
        setVersion(null);
        setDescription(null);
        setContact(null);
        //     setLicense(null) // = "Apache 2.0 License";
        //     setLicenseUrl()  // = "http://www.apache.org/licenses/LICENSE-2.0.html";
    }
}
