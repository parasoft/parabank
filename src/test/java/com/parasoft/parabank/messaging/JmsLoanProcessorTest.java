package com.parasoft.parabank.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.transform.stream.StreamSource;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

public class JmsLoanProcessorTest extends AbstractParaBankDataSourceTest {
    private static final String TEST_PROVIDER = "Test Provider";

    @Resource(name = "jmsLoanProcessor")
    private JmsLoanProcessor jmsLoanProcessor;

    @Resource(name = "jaxb2Marshaller")
    private Marshaller marshaller;

    @Resource(name = "jaxb2Marshaller")
    private Unmarshaller unmarshaller;

    private LoanRequest loanRequest;

    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        loanRequest = new LoanRequest();
        loanRequest.setAvailableFunds(new BigDecimal("1000.00"));
        loanRequest.setDownPayment(new BigDecimal("100.00"));
        loanRequest.setLoanAmount(new BigDecimal("5000.00"));

        //        final ConnectionFactory connectionFactory =
        //            new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        //        jmsTemplate = new JmsTemplate();
        //        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsLoanProcessor.setJmsTemplate(jmsTemplate);
        jmsLoanProcessor.setLoanProviderName(TEST_PROVIDER);
    }

    public void setJmsLoanProcessor(final JmsLoanProcessor jmsLoanProcessor) {
        this.jmsLoanProcessor = jmsLoanProcessor;
    }

    public void setMarshaller(final Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(final Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Test
    public void testOnMessage() throws Exception {
        TextMessage message = new ActiveMQTextMessage();
        message.setText(MarshalUtil.marshal(marshaller, loanRequest));
        jmsLoanProcessor.onMessage(message);
        message = (TextMessage) jmsTemplate.receive("queue.test.response");
        final Object obj = unmarshaller.unmarshal(new StreamSource(new StringReader(message.getText())));
        assertTrue(obj instanceof LoanResponse);
        final LoanResponse response = (LoanResponse) obj;
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals(TEST_PROVIDER, response.getLoanProviderName());

        message = new ActiveMQTextMessage() {
            @Override
            public String getText() throws JMSException {
                throw new JMSException(null);
            }
        };
        jmsLoanProcessor.onMessage(message);
    }
}
