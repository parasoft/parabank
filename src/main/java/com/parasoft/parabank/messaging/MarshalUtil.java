package com.parasoft.parabank.messaging;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

/**
 * Utility class for marshalling/unmarshalling JAXB objects
 */
public final class MarshalUtil {
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
