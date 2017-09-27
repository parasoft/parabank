package com.parasoft.parabank.test.util;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.support.*;

// @SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/**/test-context.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })

public abstract class AbstractParaBankTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Autowired
    private ApplicationContext applicationContext;

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
    };

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
    };

    @Before
    public void setUp() throws Exception {
    };

    @After
    public void tearDown() throws Exception {
    };

    // extends AbstractTransactionalJUnit4SpringContextTests {
    // @Override
    // protected final String[] getConfigLocations() {
    // return new String[] { "classpath:test-context.xml" };
    // }

}
