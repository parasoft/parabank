package com.parasoft.parabank.dao.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.parasoft.parabank.dao.AccountDao;
import com.parasoft.parabank.domain.Account;

/*
 * JDBC implementation of AccountDao
 */
public class JdbcAccountDao extends NamedParameterJdbcDaoSupport implements AccountDao {
    private static class AccountMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final Account account = new Account();
            account.setId(rs.getInt("id"));
            account.setCustomerId(rs.getInt("customer_id"));
            account.setIntType(rs.getInt("type"));
            final BigDecimal balance = rs.getBigDecimal("balance");
            account.setBalance(balance == null ? null : balance.setScale(2));
            return account;
        }
    }
    // private NamedParameterJdbcTemplate namedJdbcTemplate;
    //
    // /**
    // * <DL><DT>Description:</DT><DD>
    // * Getter for the namedJdbcTemplate property
    // * </DD>
    // * <DT>Date:</DT><DD>Oct 8, 2015</DD>
    // * </DL>
    // * @return the value of namedJdbcTemplate field
    // */
    // public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
    // return namedJdbcTemplate;
    // }
    //
    // /**
    // * <DL><DT>Description:</DT><DD>
    // * Setter for the namedJdbcTemplate property
    // * </DD>
    // * <DT>Date:</DT><DD>Oct 8, 2015</DD>
    // * </DL>
    // * @param aNamedJdbcTemplate new value for the namedJdbcTemplate property
    // */
    // public void setNamedJdbcTemplate(NamedParameterJdbcTemplate
    // aNamedJdbcTemplate) {
    // namedJdbcTemplate = aNamedJdbcTemplate;
    // }

    private static final Logger log = LoggerFactory.getLogger(JdbcAccountDao.class);

    private JdbcSequenceDao sequenceDao;

    // /** {@inheritDoc} */
    // protected void initTemplateConfig() {
    // DataSource ds = getDataSource();
    // if (ds!=null) {
    // NamedJgetNamedJdbcTemplate();
    // if (getNamedJdbcTemplate()!=null) {
    //
    // }
    // }
    // }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AccountDao#createAccount(com.parasoft.parabank. domain.Account)
     */
    @Override
    public int createAccount(final Account account) {
        final String SQL =
            "INSERT INTO Account (id, customer_id, type, balance) VALUES (:id, :customerId, :intType, :balance)";

        final int id = sequenceDao.getNextId("Account");
        account.setId(id);
        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(account);
        getNamedParameterJdbcTemplate().update(SQL, source);
        // getJdbcTemplate().update(SQL, account.getId(),
        // account.getCustomerId(), account.getIntType(),
        // account.getBalance());
        log.info("Created new account with id = " + id);

        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AccountDao#getAccount(int)
     */
    @Override
    public Account getAccount(final int id) {
        final String SQL = "SELECT id, customer_id, type, balance FROM Account WHERE id = ?";

        log.info("Getting account object for id = " + id);
        final Account account = getJdbcTemplate().queryForObject(SQL, new AccountMapper(), id);

        return account;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AccountDao#getAccountsForCustomerId(int)
     */
    @Override
    public List<Account> getAccountsForCustomerId(final int customerId) {
        final String SQL = "SELECT id, customer_id, type, balance FROM Account WHERE customer_id = ?";

        final List<Account> accounts = getJdbcTemplate().query(SQL, new AccountMapper(), customerId);
        log.info("Retrieved " + accounts.size() + " accounts for customerId = " + customerId);

        return accounts;
    }

    public void setSequenceDao(final JdbcSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AccountDao#updateAccount(com.parasoft.parabank. domain.Account)
     */
    @Override
    public void updateAccount(final Account account) {
        final String SQL =
            "UPDATE Account SET customer_id = :customerId, type = :intType, balance = :balance WHERE id = :id";

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(account);
        getNamedParameterJdbcTemplate().update(SQL, source);
        // getJdbcTemplate().update(SQL, account.getCustomerId(),
        // account.getIntType(), account.getBalance(),
        // account.getId());
        log.info("Updated information for account with id = " + account.getId());
    }
}
