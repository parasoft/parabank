package com.parasoft.parabank.service;

import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.wsdl.service.factory.DefaultServiceConfiguration;

/**
 * Defines parameters for XML Schema generation by CXF
 */
public class ParaBankServiceConfiguration extends DefaultServiceConfiguration {

    /** {@inheritDoc} */
    @Override
    public Long getWrapperPartMinOccurs(final MessagePartInfo mpi) {
        // minOccurs should always = 1 so input parameters are not optional
        return 1L;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean isWrapperPartNillable(final MessagePartInfo mpi) {
        // input parameters should never be nillable
        return false;
    }
}
