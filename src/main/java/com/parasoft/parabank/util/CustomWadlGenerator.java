package com.parasoft.parabank.util;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.wadl.WadlGenerator;
import org.apache.cxf.message.Message;

import io.swagger.jaxrs.listing.ApiListingResource;

public class CustomWadlGenerator extends WadlGenerator {
    @Override
    public List<ClassResourceInfo> getResourcesList(Message m, UriInfo ui) {
        List<ClassResourceInfo> cris = super.getResourcesList(m, ui);
        Iterator<ClassResourceInfo> it = cris.iterator();
        while (it.hasNext()) {
            // Filter out ApiListingResource, which does not make sense to include in our WADL.
            if (ApiListingResource.class.isAssignableFrom(it.next().getResourceClass())) {
                it.remove();
            }
        }
        return cris;
    }
}
