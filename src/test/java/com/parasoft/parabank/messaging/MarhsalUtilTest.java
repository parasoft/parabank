package com.parasoft.parabank.messaging;

import static org.junit.Assert.*;

import java.io.*;

import javax.annotation.*;
import javax.xml.transform.*;

import org.junit.*;
import org.springframework.oxm.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-40
 *
 */
public class MarhsalUtilTest extends AbstractParaBankDataSourceTest {
    private static final String XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><loanRequest xmlns:ns2=\"http://service.parabank.parasoft.com/\"><customerId>0</customerId></loanRequest>";

    @Resource(name = "jaxb2Marshaller")
    private Marshaller marshaller;

    @Resource(name = "jaxb2Marshaller")
    private Unmarshaller unmarshaller;

    public void setMarshaller(final Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(final Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Test
    public void testMarshal() {
        String xml = MarshalUtil.marshal(marshaller, new LoanRequest());
        assertEquals(XML, xml);

        xml = MarshalUtil.marshal(new Marshaller() {
            @Override
            public void marshal(final Object graph, final Result result) throws IOException, XmlMappingException {
                throw new IOException();
            }

            @Override
            public boolean supports(final Class<?> clazz) {
                return true;
            }
        }, new LoanRequest());
        assertEquals(0, xml.length());
    }

    @Test
    public void testUnmarshal() {
        Object obj = MarshalUtil.unmarshal(unmarshaller, XML);
        assertTrue(obj instanceof LoanRequest);

        obj = MarshalUtil.unmarshal(new Unmarshaller() {
            @Override
            public boolean supports(final Class<?> clazz) {
                return true;
            }

            @Override
            public Object unmarshal(final Source source) throws IOException, XmlMappingException {
                throw new IOException();
            }
        }, XML);
        assertNull(obj);
    }
}
