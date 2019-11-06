package com.parasoft.bookstore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.parasoft.parabank.test.util.AbstractParaBankTest;

public abstract class AbstractCartService extends AbstractParaBankTest {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CartServiceTest.class);
    private static CartService service;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add setUpBeforeClass description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 6, 2015</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        //TODO: add this to the Spring configuration xml
        setService(new CartService());
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add tearDownAfterClass description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 6, 2015</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    public AbstractCartService() {
        super();
    }

    protected int getCurrentCartId() {
        int cartId = 0;
        try {
            CartService cartService = getService();
            cartId = cartService.getStaticCart_Id();
            if (cartId > 0 ) {
                log.info("cart {} has: {} items.", cartId, cartService.getItemsInCart(cartId).getCartSize(cartId));
            }
        } catch (Exception ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1$ {0xD}
            , ex);
        }
        return cartId;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add setUp description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 6, 2015</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @Override
    @Before
    public void setUp() throws Exception {
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add tearDown description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 6, 2015</DD>
     * </DL>
     *
     * @throws java.lang.Exception
     */
    @Override
    @After
    public void tearDown() throws Exception {
    }

    /**
     * <DL><DT>Description:</DT><DD>
     * Getter for the service property
     * </DD>
     * <DT>Date:</DT><DD>Oct 7, 2015</DD>
     * </DL>
     * @return the value of service field
     */
    public static CartService getService() {
        return service;
    }

    /**
     * <DL><DT>Description:</DT><DD>
     * Setter for the service property
     * </DD>
     * <DT>Date:</DT><DD>Oct 7, 2015</DD>
     * </DL>
     * @param aService new value for the service property
     */
    public static void setService(CartService aService) {
        service = aService;
    }

}
