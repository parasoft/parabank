/**
 *
 */
package com.parasoft.bookstore2;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Parasoft Jtest UTA: Test class for Order
 *
 * @see com.parasoft.bookstore2.Order
 * @author bmcglau
 */
public class OrderTest {

    @InjectMocks
    Order underTest;

    @Mock
    Book book;

    private AutoCloseable closeable;

    @Before
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    /**
     * Parasoft Jtest UTA: Test for getDescription()
     *
     * @see com.parasoft.bookstore2.Order#getDescription()
     * @author bmcglau
     */
    @Test
    public void testGetDescription() throws Throwable {
        ProductInfo getProductInfoResult = mock(ProductInfo.class);
        when(getProductInfoResult.toString()).thenReturn("book");
        when(book.getProductInfo()).thenReturn(getProductInfoResult);
        // When
        String result = underTest.getDescription();

        // Then
        assertTrue(result.startsWith("Order: book x0 on "));
    }
}
