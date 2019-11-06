package com.parasoft.parabank.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.LoanProvider;

/**
 * Message listener that delegates to a loan processor to handle incoming loan requests over JMS
 */
public class JmsLoanProcessor implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(JmsLoanProcessor.class);

    private JmsTemplate jmsTemplate;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    private LoanProvider loanProcessor;

    private String destinationName;

    private String loanProviderName;

    private String getLoanResponseMessage(final LoanRequest loanRequest) {
        final LoanResponse response = loanProcessor.requestLoan(loanRequest);
        response.setLoanProviderName(loanProviderName);

        return MarshalUtil.marshal(marshaller, response);
    }

    @Override
    public void onMessage(final Message message) {
        try {
            final String text = ((TextMessage) message).getText();
            final Object obj = MarshalUtil.unmarshal(unmarshaller, text);
            final LoanRequest loanRequest = (LoanRequest) obj;

            jmsTemplate.send(destinationName, (MessageCreator) session -> session.createTextMessage(getLoanResponseMessage(loanRequest)));
        } catch (final JMSException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e);
        }
    }

    public void setDestinationName(final String destinationName) {
        this.destinationName = destinationName;
    }

    public void setJmsTemplate(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setLoanProcessor(final LoanProvider loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    public void setLoanProviderName(final String loanProviderName) {
        this.loanProviderName = loanProviderName;
    }

    public void setMarshaller(final Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(final Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }
}
