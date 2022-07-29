package com.parasoft.parabank.dao.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.parasoft.parabank.dao.TransactionDao;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;

/*
 * JDBC implementation of TransactionDao
 */
public class JdbcTransactionDao extends NamedParameterJdbcDaoSupport implements TransactionDao {
    private static class TransactionMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final Transaction transaction = new Transaction();
            transaction.setId(rs.getInt("id"));
            transaction.setAccountId(rs.getInt("account_id"));
            transaction.setIntType(rs.getInt("type"));
            transaction.setDate(rs.getDate("date"));
            final BigDecimal amount = rs.getBigDecimal("amount");
            transaction.setAmount(amount == null ? null : amount.setScale(2));
            transaction.setDescription(rs.getString("description"));
            return transaction;
        }
    }

    protected static final Logger log = LoggerFactory.getLogger(JdbcTransactionDao.class);

    private JdbcSequenceDao sequenceDao;

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.TransactionDao#createTransaction(com.parasoft. parabank.domain.Transaction)
     */
    @Override
    public int createTransaction(final Transaction transaction) {
        final String SQL =
            "INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (:id, :accountId, :intType, :date, :amount, :description)";

        final int id = sequenceDao.getNextId("Transaction");
        transaction.setId(id);
        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(transaction);
        getNamedParameterJdbcTemplate().update(SQL, source);

        // getJdbcTemplate().update(SQL, new
        // BeanPropertySqlParameterSource(transaction));
        log.info("Created new transaction with id = " + id);

        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.TransactionDao#getTransaction(int)
     */
    @Override
    public Transaction getTransaction(final int id) {
        final String SQL =
            "SELECT id, account_id, type, date, amount, description FROM Transaction WHERE id = ? ORDER BY date";

        log.info("Getting transaction object for id = " + id);
        final Transaction transaction = getJdbcTemplate().queryForObject(SQL, new TransactionMapper(), id);

        return transaction;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.TransactionDao#getTransactionsForAccount(int)
     */
    @Override
    public List<Transaction> getTransactionsForAccount(final int accountId) {
        // Return in chronological order.
        final String SQL =
            "SELECT id, account_id, type, date, amount, description FROM Transaction WHERE account_id = ? ORDER BY date, id";

        final List<Transaction> transactions = getJdbcTemplate().query(SQL, new TransactionMapper(), accountId);
        log.info("Retrieved " + transactions.size() + " transactions for accountId = " + accountId);

        return transactions;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.TransactionDao#getTransactionsForAccount(int,
     * com.parasoft.parabank.domain.TransactionCriteria)
     */
    @Override
    public List<Transaction> getTransactionsForAccount(final int accountId, final TransactionCriteria criteria) {
        String SQL =
            "SELECT id, account_id, type, date, amount, description, MONTH(date) as month FROM Transaction WHERE account_id = ?";

        final List<Object> params = new ArrayList<>();
        params.add(accountId);

        SQL += getRestrictions(criteria, params);

        // Return in chronological order.
        SQL += " ORDER BY date, id";

        final List<Transaction> transactions = getJdbcTemplate().query(SQL, new TransactionMapper(), params.toArray());
        log.info("Retrieved " + transactions.size() + " transactions for accountId = " + accountId
            + " with search type = " + criteria.getSearchType());

        return transactions;
    }

    protected String getRestrictions(final TransactionCriteria criteria, final List<Object> params) {
        return new JdbcTransactionQueryRestrictor().getRestrictions(criteria, params);
    }

    public void setSequenceDao(final JdbcSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }
}
