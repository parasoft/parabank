package com.parasoft.parabank.messaging;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
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
 * Message client for generating and sending loan requests over JMS
 */
public class JmsLoanProvider implements LoanProvider {
    private static final Logger log = LoggerFactory.getLogger(JmsLoanProvider.class);

    private JmsTemplate jmsTemplate;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    private String requestDesinationName;

    private String responseDestinationName;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the jmsTemplate property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 20, 2015</DD>
     * </DL>
     *
     * @return the value of jmsTemplate field
     */
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the marshaller property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 20, 2015</DD>
     * </DL>
     *
     * @return the value of marshaller field
     */
    public Marshaller getMarshaller() {
        return marshaller;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the requestDesinationName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 20, 2015</DD>
     * </DL>
     *
     * @return the value of requestDesinationName field
     */
    public String getRequestDesinationName() {
        return requestDesinationName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the responseDestinationName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 20, 2015</DD>
     * </DL>
     *
     * @return the value of responseDestinationName field
     */
    public String getResponseDestinationName() {
        return responseDestinationName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the unmarshaller property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 20, 2015</DD>
     * </DL>
     *
     * @return the value of unmarshaller field
     */
    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    LoanResponse processResponse(final Message message) {
        if (message != null) {
            try {
                final String text = ((TextMessage) message).getText();
                final Object obj = MarshalUtil.unmarshal(unmarshaller, text);
                return (LoanResponse) obj;
            } catch (final JMSException e) {
                log.error("{} caught :", e.getClass().getSimpleName(), e);
            }
        } else {
            log.error(
                "Did not receive response message within timeout period of " + jmsTemplate.getReceiveTimeout() + " ms");
            final LoanResponse response = new LoanResponse();
            response.setResponseDate(new Date());
            response.setApproved(false);
            response.setMessage("error.timeout");
            return response;
        }
        return null;
    }

    @Override
    public LoanResponse requestLoan(final LoanRequest loanRequest) {
        jmsTemplate.send(requestDesinationName, (MessageCreator) session -> {
            final String xml = MarshalUtil.marshal(marshaller, loanRequest);
            return session.createTextMessage(xml);
        });
        final Message message = jmsTemplate.receive(responseDestinationName);
        return processResponse(message);
    }

    public void setJmsTemplate(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setMarshaller(final Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the requestDesinationName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 20, 2015</DD>
     * </DL>
     *
     * @param aRequestDesinationName
     *            new value for the requestDesinationName property
     */
    public void setRequestDesinationName(final String aRequestDesinationName) {
        requestDesinationName = aRequestDesinationName;
    }

    public void setRequestDestinationName(final String requestDesinationName) {
        this.requestDesinationName = requestDesinationName;
    }

    public void setResponseDestinationName(final String responseDestinationName) {
        this.responseDestinationName = responseDestinationName;
    }

    public void setUnmarshaller(final Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }
}
