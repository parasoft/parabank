package com.parasoft.parabank.util;

import java.util.*;

import javax.ws.rs.core.*;

import org.apache.cxf.jaxrs.model.*;
import org.apache.cxf.jaxrs.model.wadl.*;
import org.apache.cxf.message.*;

import io.swagger.jaxrs.listing.*;

public class CustomWadlGenerator extends WadlGenerator {

    @Override
    public List<ClassResourceInfo> getResourcesList(Message m, UriInfo ui) {
        List<ClassResourceInfo> resources = super.getResourcesList(m, ui);
        // Must return a copy.  Otherwise, removing ApiListingResource from the
        // original list would remove the resource from JAX-RS entirely.
        List<ClassResourceInfo> copy = new ArrayList<>(resources.size());
        for (ClassResourceInfo resource : resources) {
            // Filter out ApiListingResource, which does not make sense to include in our WADL.
            if (!ApiListingResource.class.isAssignableFrom(resource.getResourceClass())) {
                copy.add(resource);
            }
        }
        return copy;
    }
}
