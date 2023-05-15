package com.parasoft.parabank.test.util;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.UserSession;

// @SuppressWarnings("deprecation")
@WebAppConfiguration("file:src/test/resources")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/**/test-context.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@Transactional
public abstract class AbstractParaBankDataSourceTest extends JdbcDaoSupport {
    private final static Logger log = LoggerFactory.getLogger(AbstractParaBankDataSourceTest.class);

    // public static final String AMMOUNT_MATCH_REGEX =
    // ".*%1$s.00</loanAmount.*";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>parse passed string to date YYYY-MM-DD</DD>
     * <DT>Date:</DT>
     * <DD>Oct 20, 2015</DD>
     * </DL>
     *
     * @param date
     * @param dateValue
     * @return
     */
    public static java.sql.Date convertDate(final String dateValue) {
        java.sql.Date date = null;

        try {
            date = java.sql.Date.valueOf(dateValue);
            //new java.sql.Date(getDateFormat().parse(dateValue).getTime());
        } catch (final IllegalArgumentException ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1$ {0xD}
                , ex);
        }
        assertNotNull(date);
        return date;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the dateFormat property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @return the value of dateFormat field
     */
    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Autowired
    private ApplicationContext applicationContext;

    private String sqlScriptEncoding;

    protected MockHttpServletRequest request;

    protected MockHttpServletResponse response;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private String path;

    private String formName;

    private String resultClassName = BindingResult.class.getName();

    private Map<String, String> errorMap = new HashMap<>();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(19089);

    @javax.annotation.Resource(name = "resources")
    private List<LoanRequestTestConfig> resources;

    @Resource(name = "adminDao")
    private AdminDao adminDao;

    private Map<String, String> testResponses = new HashMap<>();

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add assertError description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param mav
     * @param errorCount
     * @param fieldErrors
     */
    protected void assertError(final ModelAndView mav, final int errorCount, final Map<String, String> fieldErrors) {
        final String errorName = String.format("%1$s.%2$s", getResultClassName(), getFormName());
        final BindingResult errors = (BindingResult) mav.getModel().get(errorName);
        assertEquals(errorCount, errors.getErrorCount());
        for (final String fieldName : fieldErrors.keySet()) {
            final FieldError fe = errors.getFieldError(fieldName);
            assertNotNull("missing FieldError for " + fieldName, fe);
            assertEquals(fieldName, fe.getField());
            assertEquals(fieldErrors.get(fieldName), fe.getCode());
        }
        // return errorName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add assertError description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param mav
     * @param fieldErrors
     */
    protected void assertError(final ModelAndView mav, final Map<String, String> fieldErrors) {
        assertError(mav, fieldErrors.size(), fieldErrors);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add assertError description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param mav
     * @param fieldName
     * @param errorCode
     */
    protected void assertError(final ModelAndView mav, final String fieldName, final String errorCode) {
        final Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put(fieldName, errorCode);
        assertError(mav, 1, fieldErrors);
    }

    /**
     * Execute the given SQL script. Use with caution outside of a transaction!
     * <p>
     * The script will normally be loaded by classpath. There should be one statement per line. Any semicolons will be removed. <b>Do not use this
     * method to execute DDL if you expect rollback.</b>
     *
     * @param sqlResourcePath
     *            the Spring resource path for the SQL script
     * @param continueOnError
     *            whether or not to continue without throwing an exception in the event of an error
     * @throws DataAccessException
     *             if there is an error executing a statement and continueOnError was {@code false}
     */
    protected void executeSqlScript(final String sqlResourcePath, final boolean continueOnError) throws DataAccessException {

        final org.springframework.core.io.Resource resource = applicationContext.getResource(sqlResourcePath);
        ScriptUtils.executeSqlScript(getConnection(), new EncodedResource(resource, getSqlScriptEncoding()));
        // ,continueOnError);
        // JdbcTestUtils.executeSqlScript(getJdbcTemplate(), new
        // EncodedResource(resource, this.getSqlScriptEncoding()),
        // continueOnError);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the adminDao property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of adminDao field
     */
    public AdminDao getAdminDao() {
        return adminDao;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the applicationContext property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of applicationContext field
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the errorMap property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @return the value of errorMap field
     */
    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the formName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @return the value of formName field
     */
    public String getFormName() {
        return formName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the handlerAdapter property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 13, 2015</DD>
     * </DL>
     *
     * @return the value of handlerAdapter field
     */
    public RequestMappingHandlerAdapter getHandlerAdapter() {
        return handlerAdapter;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the handlerMapping property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 13, 2015</DD>
     * </DL>
     *
     * @return the value of handlerMapping field
     */
    public RequestMappingHandlerMapping getHandlerMapping() {
        return handlerMapping;
    }

    protected final Object getModelValue(final ModelAndView mav, final String name) {
        final ModelMap model = mav.getModelMap();
        @SuppressWarnings("unchecked")
        final Map<String, Object> map = (Map<String, Object>) model.get("model");
        return map.get(name);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the path property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 14, 2015</DD>
     * </DL>
     *
     * @return the value of path field
     */
    public String getPath() {
        return path;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the resources property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @return the value of resources field
     */
    public List<LoanRequestTestConfig> getResources() {
        return resources;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the resultClassName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @return the value of resultClassName field
     */
    public String getResultClassName() {
        return resultClassName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the sqlScriptEncoding property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of sqlScriptEncoding field
     */
    public String getSqlScriptEncoding() {
        return sqlScriptEncoding;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the testResponses property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @return the value of testResponses field
     */
    public Map<String, String> getTestResponses() {
        return testResponses;
    }

    @Before
    public void onSetUp() throws Exception {
        log.trace("in AbstractParaBankDataSourceTest.onSetUp()");
        request = new MockHttpServletRequest();
        request.setMethod("GET");

        response = new MockHttpServletResponse();

        if (getTestResponses().isEmpty()) {
            getAdminDao().setParameter("endpoint", "http://localhost:19089/parabank/services/LoanProcessor");
            for (final LoanRequestTestConfig key : getResources()) {
                if (key.isLoanRequest()) {
                    stubFor(post(urlPathMatching("/parabank/services/LoanProcessor")).atPriority(10)
                        .withRequestBody(matching(String.format(LoanRequestTestConfig.AMMOUNT_MATCH_REGEX, key.getAmount()))).willReturn(
                            aResponse().withStatus(200).withHeader("Content-Type", "text/xml;charset=UTF-8").withBody(key.getResponseData())));
                } else {
                    if (key.getName().equalsIgnoreCase("wsdl")) {
                        stubFor(get(urlPathEqualTo("/wsdl/parabank/services/LoanProcessor")).atPriority(100000).withQueryParam("wsdl", containing(""))
                            .willReturn(
                                aResponse().withStatus(200).withHeader("Content-Type", "text/xml;charset=UTF-8").withBody(key.getResponseData())));
                    }
                }
                getTestResponses().put(key.getName(), key.getResponseData());
                log.info("loaded requestLoan mock for {}", key.getName());
            }

        }
    }

    @BeforeTransaction
    public void onSetUpInTransaction() throws Exception {
        // executeSqlScript("classpath:com/parasoft/parabank/dao/jdbc/sql/create.sql",
        // true);
        // executeSqlScript("classpath:com/parasoft/parabank/dao/jdbc/sql/insert.sql",
        // true);
    }

    @After
    public void onTearDown() throws Exception {
    }

    protected ModelAndView processGetRequest(final MockHttpServletRequest aRequest, final MockHttpServletResponse aResponse)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
        Object handler;
        ModelAndView mav;
        aRequest.setMethod("GET");
        aRequest.setServletPath(getPath());
        final RequestMappingHandlerMapping mapping = getHandlerMapping();
        final HandlerExecutionChain chain = mapping.getHandler(aRequest);
        handler = chain.getHandler();
        mav = getHandlerAdapter().handle(aRequest, aResponse, handler);
        return mav;
    }

    protected ModelAndView processPostRequest(final Object form, final MockHttpServletRequest aRequest, final MockHttpServletResponse aResponse)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
        Object handler;
        ModelAndView mav;
        aRequest.setMethod("POST");
        aRequest.setServletPath(getPath());
        if (form != null) {
            aRequest.getSession().setAttribute(getFormName(), form);
        }
        // aRequest.setParameters(BeanUtils.describe(form));
        handler = getHandlerMapping().getHandler(aRequest).getHandler();
        mav = getHandlerAdapter().handle(aRequest, aResponse, handler);
        return mav;
    }

    protected final MockHttpServletRequest registerSession(final MockHttpServletRequest aRequest) {
        return registerSession(aRequest, 12212);
    }

    protected final MockHttpServletRequest registerSession(final MockHttpServletRequest aRequest, final int custId) {
        final MockHttpSession session = new MockHttpSession();
        final Customer customer = new Customer();
        customer.setId(custId);
        session.setAttribute(Constants.USERSESSION, new UserSession(customer));
        aRequest.setSession(session);
        return aRequest;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the adminDao property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param adminDao
     *            new value for the adminDao property
     */
    public void setAdminDao(final AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the applicationContext property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @param aApplicationContext
     *            new value for the applicationContext property
     */
    public void setApplicationContext(final ApplicationContext aApplicationContext) {
        applicationContext = aApplicationContext;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the errorMap property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param aErrorMap
     *            new value for the errorMap property
     */
    public void setErrorMap(final Map<String, String> aErrorMap) {
        errorMap = aErrorMap;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the formName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 13, 2015</DD>
     * </DL>
     *
     * @param aFormName
     *            new value for the formName property
     */
    public void setFormName(final String aFormName) {
        formName = aFormName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the handlerAdapter property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 13, 2015</DD>
     * </DL>
     *
     * @param aHandlerAdapter
     *            new value for the handlerAdapter property
     */
    public void setHandlerAdapter(final RequestMappingHandlerAdapter aHandlerAdapter) {
        handlerAdapter = aHandlerAdapter;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the handlerMapping property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 13, 2015</DD>
     * </DL>
     *
     * @param aHandlerMapping
     *            new value for the handlerMapping property
     */
    public void setHandlerMapping(final RequestMappingHandlerMapping aHandlerMapping) {
        handlerMapping = aHandlerMapping;
    }

    @Resource(name = "dataSource")
    public void setParentDataSource(final DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the path property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 14, 2015</DD>
     * </DL>
     *
     * @param aPath
     *            new value for the path property
     */
    public void setPath(final String aPath) {
        path = aPath;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the resources property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @param resources
     *            new value for the resources property
     */
    public void setResources(final List<LoanRequestTestConfig> resources) {
        this.resources = resources;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the resultClassName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param aResultClassName
     *            new value for the resultClassName property
     */
    public void setResultClassName(final String aResultClassName) {
        resultClassName = aResultClassName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the sqlScriptEncoding property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @param aSqlScriptEncoding
     *            new value for the sqlScriptEncoding property
     */
    public void setSqlScriptEncoding(final String aSqlScriptEncoding) {
        sqlScriptEncoding = aSqlScriptEncoding;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the testResponses property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 7, 2016</DD>
     * </DL>
     *
     * @param testResponses
     *            new value for the testResponses property
     */
    public void setTestResponses(final Map<String, String> testResponses) {
        this.testResponses = testResponses;
    }

    // extends AbstractTransactionalJUnit4SpringContextTests {
    // @Override
    // protected final String[] getConfigLocations() {
    // return new String[] { "classpath:test-context.xml" };
    // }

}
