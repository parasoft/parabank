package com.parasoft.parabank.messaging;

import static org.junit.Assert.*;

import javax.annotation.*;
import javax.jms.*;
import javax.jms.Message;

import org.apache.activemq.command.*;
import org.junit.*;
import org.springframework.jms.core.*;
import org.springframework.oxm.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-38
 *
 */
public class JmsLoanProviderTest extends AbstractParaBankDataSourceTest {
    @Resource(name = "jmsLoanProvider")
    private JmsLoanProvider jmsLoanProvider;

    @Resource(name = "jaxb2Marshaller")
    private Marshaller marshaller;

    private JmsTemplate jmsTemplate;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();

        //        final ConnectionFactory connectionFactory =
        //            new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        //        jmsTemplate = new JmsTemplate();
        //        jmsTemplate.setConnectionFactory(connectionFactory);
        //jmsLoanProvider.setJmsTemplate(jmsTemplate);
        jmsTemplate = jmsLoanProvider.getJmsTemplate();
    }

    public void setJmsLoanProvider(final JmsLoanProvider jmsLoanProvider) {
        this.jmsLoanProvider = jmsLoanProvider;
    }

    public void setMarshaller(final Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @Test
    public void testRequestLoan() {
        jmsTemplate.send("queue.test.response", new MessageCreator() {
            @Override
            public Message createMessage(final Session session) throws JMSException {
                final LoanResponse loanResponse = new LoanResponse();
                loanResponse.setApproved(true);
                final TextMessage message = session.createTextMessage();
                message.setText(MarshalUtil.marshal(marshaller, loanResponse));
                return message;
            }
        });
        final LoanResponse loanResponse = jmsLoanProvider.requestLoan(new LoanRequest());
        assertTrue(loanResponse.isApproved());
    }

    @Test
    public void testRequestLoanException() {
        final TextMessage message = new ActiveMQTextMessage() {
            @Override
            public String getText() throws JMSException {
                throw new JMSException(null);
            }
        };
        final LoanResponse loanResponse = jmsLoanProvider.processResponse(message);
        assertNull(loanResponse);
    }

    @Test
    public void testRequestLoanTimeout() {
        jmsTemplate.setReceiveTimeout(1);
        final LoanResponse loanResponse = jmsLoanProvider.requestLoan(new LoanRequest());
        assertFalse(loanResponse.isApproved());
        assertNotNull(loanResponse.getResponseDate());
        assertEquals("error.timeout", loanResponse.getMessage());
    }
}
