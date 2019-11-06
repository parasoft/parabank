/*
 * (C) Copyright Parasoft Corporation 2017.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.parabank.dao.jdbc;

import java.util.List;

import com.parasoft.parabank.domain.Transaction.TransactionType;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;

public class SecureJdbcTransactionDao extends JdbcTransactionDao {

    @Override
    protected String getRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        JdbcTransactionQueryRestrictor transQueryRestrictor = new JdbcTransactionQueryRestrictor();
        if (criteria.getSearchType() == SearchType.ACTIVITY) {
            return getActivityRestrictions(transQueryRestrictor, criteria, params);
        }
        return transQueryRestrictor.getRestrictions(criteria, params);
    }

    private String getActivityRestrictions(JdbcTransactionQueryRestrictor transQueryRestrictor,
            final TransactionCriteria criteria, final List<Object> params) {
        String restrictionsSql = transQueryRestrictor.getCommonActivityRestrictions(criteria, params);

        if (criteria.getTransactionType() != null && !"All".equals(criteria.getTransactionType())) {
            restrictionsSql += " AND TYPE = ?";
            params.add(TransactionType.valueOf(criteria.getTransactionType()).ordinal());
        }

        log.info("Searching transactions by activity with parameters: " + params);
        return restrictionsSql;
    }
}
