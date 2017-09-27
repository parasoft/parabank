package com.parasoft.parabank.messaging;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.slf4j.*;
import org.springframework.oxm.*;

/**
 * Utility class for marshalling/unmarshalling JAXB objects
 */
public class MarshalUtil {
    private static final Logger log = LoggerFactory.getLogger(MarshalUtil.class);

    /**
     * Convert a JAXB object to XML
     *
     * @param marshaller
     *            the marshaller used to perform the conversion
     * @param obj
     *            the object to marshal
     * @return XML representation of the object
     */
    public static String marshal(final Marshaller marshaller, final Object obj) {
        final Writer writer = new StringWriter();
        try {
            marshaller.marshal(obj, new StreamResult(writer));
        } catch (final IOException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e);
        }
        return writer.toString();
    }

    /**
     * Convert XML to a JAXB object
     *
     * @param marshaller
     *            the unmarshaller used to perform the conversion
     * @param xml
     *            the xml to unmarshal
     * @return JAXB object represented by the XML
     */
    public static Object unmarshal(final Unmarshaller unmarshaller, final String xml) {
        final Source source = new StreamSource(new StringReader(xml));
        try {
            return unmarshaller.unmarshal(source);
        } catch (final IOException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e);
        }
        return null;
    }

    private MarshalUtil() {
    }
}
