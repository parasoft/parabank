/*
 * (C) Copyright Parasoft Corporation 2019.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.parabank.service;

import java.math.BigDecimal;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.parasoft.parabank.domain.BillPayResult;
import com.parasoft.parabank.domain.Payee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface IBillPayService extends ParaBankServiceConstants {

    /**
     * Pay bill using funds from the given account
     *
     * @param accountId the account to which to pay the bill
     * @param amount    bill amount
     * @return status message of result
     * @throws ParaBankServiceException
     */
    @POST
    @Path("/billpay")
    @Operation(summary = "Pay bill", tags = { ParaBankServiceConstants.ACCOUNTS })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    BillPayResult billPay(
            @Parameter(description = BILL_PAY_ACCOUNT_ID_DESC, required = true) @QueryParam(ACCOUNT_ID) int accountId,
            @Parameter(description = AMOUNT_DESC, required = true) @QueryParam("amount") BigDecimal amount,
            @Parameter(description = "Payee", required = true) Payee Payee) throws ParaBankServiceException;

}
