package com.parasoft.parabank.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.parasoft.parabank.domain.LoanResponse;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>a bean used to configure the WireMock to test LoanRequest</DD>
 * <DT>Date:</DT>
 * <DD>Jun 7, 2016</DD>
 * </DL>
 *
 * @author dev - Nick Rapoport
 * @req PAR-44
 *
 */
public class LoanRequestTestConfig implements InitializingBean {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(LoanRequestTestConfig.class);

    public static final Set<String> NOT_AMOUNTS = new HashSet<>(Arrays.asList(new String[] { "wsdl" }));

    public static final String AMMOUNT_MATCH_REGEX = ".*%1$s</loanAmount.*";

    private String amount;

    private String downPayment;

    private Resource response;

    private String responseData;

    private boolean success = true;

    private String message = "";

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>LoanRequestTestConfig Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     */
    public LoanRequestTestConfig() {
        // Nothing to do here
    }

    /** {@inheritDoc} */
    @Override
    public void afterPropertiesSet() throws IOException {
        if (getAmount() == null || getAmount().isEmpty()) {
            final IOException ex = new IOException("amount/name is a required property");
            ex.fillInStackTrace();
            throw ex;
        }
        if (isLoanRequest()) {
            final BigDecimal bd = getAmountBigDecimal();
            bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            setAmount(bd.toPlainString());
            if (getDownPayment() == null || getDownPayment().isEmpty()) {
                final BigDecimal downPayment = bd.divide(new BigDecimal(10), 2, BigDecimal.ROUND_HALF_EVEN);
                downPayment.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                setDownPayment(downPayment.toPlainString());
            }
        }

        if (getResponse() == null || !getResponse().exists()) {
            final IOException ex = new IOException("response is a required and must exist");
            ex.fillInStackTrace();
            throw ex;
        }
        final String results = FileUtils.readFileToString(getResponse().getFile(), Charset.defaultCharset());
        LOG.trace("loading key {} value {} ", getAmount(), results);
        setResponseData(results);

    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the amount property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of amount field
     */
    public String getAmount() {
        return amount;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>for the amount property expressed as BigDecimal</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of amount field
     */
    public BigDecimal getAmountBigDecimal() {
        if (isLoanRequest()) {
            final String bigDec = getAmount().contains(".") ? getAmount() : getAmount() + ".00";
            return new BigDecimal(bigDec);
        }
        return new BigDecimal(0l);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the downPayment property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of downPayment field
     */
    public String getDownPayment() {
        return downPayment;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>for the downPayment property expressed as BigDecimal</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of amount field
     */
    public BigDecimal getDownPaymentBigDecimal() {
        if (isLoanRequest()) {
            final String bigDec = getDownPayment().contains(".") ? getDownPayment() : getDownPayment() + ".00";
            return new BigDecimal(bigDec);
        }
        return new BigDecimal(0L);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the message property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of message field
     */
    public String getMessage() {
        return message;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>name is just an alias for amount</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the name of this LoanRequestTestConfig
     */
    public String getName() {
        return getAmount();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the response property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of response field
     */
    public Resource getResponse() {
        return response;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the responseData property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of responseData field
     */
    public String getResponseData() {
        return responseData;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>whether this configuration is an loan request of an administrative
     * type responder configuration (like WSDL)</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return
     *         <DL>
     *         <DT><code>true</code></DT>
     *         <DD>this is a loan request type configuration</DD>
     *         <DT><code>false</code></DT>
     *         <DD>this is an administrative configuration (WSDL)</DD>
     *         </DL>
     */
    public boolean isLoanRequest() {
        return !NOT_AMOUNTS.contains(getAmount());
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the success property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of success field
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the amount property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param amount
     *            new value for the amount property
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the downPayment property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param downPayment
     *            new value for the downPayment property
     */
    public void setDownPayment(final String downPayment) {
        this.downPayment = downPayment;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the message property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param message
     *            new value for the message property
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>name is just an alias for amount</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param aName
     *            the name of this LoanRequestTestConfig
     */
    public void setName(final String aName) {
        setAmount(aName);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the response property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param response
     *            new value for the response property
     */
    public void setResponse(final Resource response) {
        this.response = response;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the responseData property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param responseData
     *            new value for the responseData property
     */
    public void setResponseData(final String responseData) {
        this.responseData = responseData;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the success property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param success
     *            new value for the success property
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add validateResults description</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param response
     * @param providerName
     */
    public void validateResults(final LoanResponse response, final String providerName) {
        if (isLoanRequest()) {
            if (isSuccess()) {
                assertTrue(response.isApproved());
            } else {
                assertFalse(response.isApproved());
            }
            assertNotNull(response.getResponseDate());
            if (!getMessage().isEmpty()) {
                assertEquals(getMessage(), response.getMessage());
            }
            assertEquals(providerName, response.getLoanProviderName());
        }
    }

}
