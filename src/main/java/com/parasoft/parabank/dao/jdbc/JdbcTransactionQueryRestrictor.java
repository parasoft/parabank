package com.parasoft.parabank.dao.jdbc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.parasoft.parabank.domain.Transaction.TransactionType;
import com.parasoft.parabank.domain.TransactionCriteria;

/**
 * Takes a transaction criteria object and returns SQL restriction clauses corresponding to the given criteria
 */
class JdbcTransactionQueryRestrictor {
    private static final Logger log = LoggerFactory.getLogger(JdbcTransactionQueryRestrictor.class);

    private String getActivityRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        String restrictionsSql = getCommonActivityRestrictions(criteria, params);

        if (criteria.getTransactionType() != null && !"All".equals(criteria.getTransactionType())) {
            String type = criteria.getTransactionType();
            try {
                type = Integer.toString(TransactionType.valueOf(criteria.getTransactionType()).ordinal());
            } catch (IllegalArgumentException e) {
                // just pass it along so we can introduce SQL injection vulnerabilities
            }
            restrictionsSql += " AND TYPE = '" + type + "'";
        }

        log.info("Searching transactions by activity with parameters: " + params);
        return restrictionsSql;
    }

    public String getCommonActivityRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        String restrictionsSql = "";

        if (criteria.getMonth() != null && !"All".equals(criteria.getMonth())) {
            try {
                final Date date = new SimpleDateFormat("MMM").parse(criteria.getMonth());
                final Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                restrictionsSql += " AND MONTH(date) = ?";
                params.add(cal.get(Calendar.MONTH) + 1);
            } catch (final ParseException e) {
                log.error("Could not parse supplied month value: " + criteria.getMonth(), e);
            }
        }
        return restrictionsSql;
    }

    private String getAmountRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        params.add(criteria.getAmount());
        log.info("Searching transactions by amount with parameters: " + params);
        return " AND amount = ?";
    }

    private String getDateRangeRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        params.add(criteria.getFromDate());
        params.add(criteria.getToDate());
        log.info("Searching transactions by date range with parameters: " + params);
        return " AND date >= ? AND date <= ?";
    }

    private String getDateRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        params.add(criteria.getOnDate());
        log.info("Searching transactions by date with parameters: " + params);
        return " AND date = ?";
    }

    private String getIdRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        params.add(criteria.getTransactionId());
        log.info("Searching transactions by id with parameters: " + params);
        return " AND id = ?";
    }

    /**
     * Create a SQL query fragment restricting transactions to the given criteria
     *
     * @param criteria
     *            the search criteria
     * @param params
     *            a set parameters to populate with criteria parameters
     * @return SQL query fragment to be appended to a WHERE clause
     */
    String getRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        String restrictionsSql = "";
        switch (criteria.getSearchType()) {
            case ACTIVITY:
                restrictionsSql = getActivityRestrictions(criteria, params);
                break;
            case ID:
                restrictionsSql = getIdRestrictions(criteria, params);
                break;
            case DATE:
                restrictionsSql = getDateRestrictions(criteria, params);
                break;
            case DATE_RANGE:
                restrictionsSql = getDateRangeRestrictions(criteria, params);
                break;
            case AMOUNT:
                restrictionsSql = getAmountRestrictions(criteria, params);
                break;
        }
        return restrictionsSql;
    }
}
