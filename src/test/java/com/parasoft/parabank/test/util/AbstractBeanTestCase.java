package com.parasoft.parabank.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

public abstract class AbstractBeanTestCase<T> extends AbstractParaBankTest {
    private static final String TEST_STRING_VAL1 = "Some Value";

    private static final String TEST_STRING_VAL2 = "Some Other Value";

    static void assertMeetsEqualsContract(final Class<?> classUnderTest) {
        Object o1;
        Object o2;
        try {
            o1 = classUnderTest.newInstance();
            o2 = classUnderTest.newInstance();

            assertTrue("Instances with default constructor not equal (o1.equals(o2))", o1.equals(o2));
            assertTrue("Instances with default constructor not equal (o2.equals(o1))", o2.equals(o1));

            final Field[] fields = classUnderTest.getDeclaredFields();
            for (final Field field : fields) {
                o1 = classUnderTest.newInstance();
                o2 = classUnderTest.newInstance();

                assertEquals("Instance o1 is not equal to itself", o1, o1);
                assertFalse("Instance o1 is equal to null", o1.equals(null)); // parasoft-suppress PB.API.EQNL "This is intended use of equals method"
                assertFalse("Instance o1 is equal to an instance of Object", o1.equals(new Object()));

                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                toggleField(field, o1, true);
                assertFalse("Instances with o1 having " + field.getName() + " set and o2 having it not set are equal", o1.equals(o2));

                field.set(o2, field.get(o1));
                assertTrue("After setting o2 with the value of the object in o1, the two objects in the field are not equal",
                    field.get(o1).equals(field.get(o2)));
                assertTrue("Instances with o1 having " + field.getName() + " set and o2 having it set to the same object of type "
                    + field.get(o2).getClass().getName() + " are not equal", o1.equals(o2));

                toggleField(field, o2, false);
                if (field.get(o1).equals(field.get(o2))) {
                    // Even though we have different instances, they are equal.
                    // Let's walk one of them
                    // to see if we can find a field to set
                    final Field[] paramFields = field.get(o1).getClass().getDeclaredFields();
                    for (Field paramField : paramFields) {
                        toggleField(paramField, field.get(o1), true);
                    }
                    assertFalse(
                        "After setting o2 with a different " + field.getName() + " than what is in o1, the two objects in the field are equal. "
                            + "This is after an attempt to walk the fields to make them different",
                        field.get(o1).equals(field.get(o2)));
                } else {
                    assertFalse("Instances with o1 having " + field.getName() + " set and o2 having it set to a different object are equal",
                        o1.equals(o2));
                }
            }
        } catch (final InstantiationException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to construct an instance of the class under test:" + classUnderTest.getName());
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to construct an instance of the class under test:" + classUnderTest.getName());
        }
    }

    static void assertMeetsHashCodeContract(final Class<?> classUnderTest) {
        try {
            final Field[] fields = classUnderTest.getDeclaredFields();
            for (final Field field : fields) {
                final Object o1 = classUnderTest.newInstance();
                final int initialHashCode = o1.hashCode();

                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                toggleField(field, o1, true);
                final int updatedHashCode = o1.hashCode();
                assertFalse("The field " + field.getName() + " was not taken into account for the hashCode contract ",
                    initialHashCode == updatedHashCode);
            }
        } catch (final InstantiationException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to construct an instance of the class under test");
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to construct an instance of the class under test");
        }
    }

    static void assertToString(final Class<?> classUnderTest) {
        Object o;
        try {
            o = classUnderTest.newInstance();
            final String str = o.toString();

            assertNotNull(str);
            assertTrue("The toString() method for class " + classUnderTest.getName() + " did not begin with the class name",
                str.startsWith(classUnderTest.getSimpleName()));

            final Field[] fields = classUnderTest.getDeclaredFields();
            for (final Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                assertTrue("The field " + field.getName() + " did not appear in toString()", str.contains(field.getName()));
            }

        } catch (final InstantiationException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to construct an instance of the class under test:" + classUnderTest.getName());
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to construct an instance of the class under test:" + classUnderTest.getName());
        }
    }

    public static void toggleField(final Field field, final Object obj, final boolean on) throws IllegalAccessException, InstantiationException {
        if (Modifier.isFinal(field.getModifiers())) {
            return; // nothing to do here
        }
        field.setAccessible(true);
        if (field.getType() == String.class) {
            field.set(obj, on ? TEST_STRING_VAL1 : TEST_STRING_VAL2);
        } else if (field.getType() == boolean.class) {
            field.setBoolean(obj, on ? true : false);
        } else if (field.getType() == short.class) {
            field.setShort(obj, on ? (short) 1 : (short) 0);
        } else if (field.getType() == long.class) {
            field.setLong(obj, on ? 1 : 0);
        } else if (field.getType() == float.class) {
            field.setFloat(obj, on ? 1 : 0);
        } else if (field.getType() == int.class) {
            field.setInt(obj, on ? 1 : 0);
        } else if (field.getType() == Integer.class) {
            field.set(obj, on ? 1 : 0);
        } else if (field.getType() == byte.class) {
            field.setByte(obj, on ? (byte) 1 : (byte) 0);
        } else if (field.getType() == char.class) {
            field.setChar(obj, on ? (char) 1 : (char) 0);
        } else if (field.getType() == double.class) {
            field.setDouble(obj, on ? 1 : 0);
        } else if (field.getType() == BigDecimal.class) {
            field.set(obj, on ? new BigDecimal(1) : new BigDecimal(0));
        } else if (field.getType() == Date.class) {
            field.set(obj, on ? new Date() : new Date(0));
        } else if (field.getType().isEnum()) {
            field.set(obj, field.getType().getEnumConstants()[on ? 1 : 0]);
        } else if (field.getType().isArray()) {
            field.set(obj, Array.newInstance(field.getType().getComponentType(), 0));
        } else if (Object.class.isAssignableFrom(field.getType())) {
            field.set(obj, field.getType().newInstance());
        } else {
            fail("Don't know how to set a " + field.getType().getName());
        }
    }

    private final Class<T> beanClass;

    protected T bean;

    @SuppressWarnings("unchecked")
    protected AbstractBeanTestCase() {
        beanClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        bean = beanClass.newInstance();
    }

    @Test
    public void testEqualsContractMet() {
        assertMeetsEqualsContract(beanClass);
    }

    @Test
    public void testHashCodeContractMet() {
        assertMeetsHashCodeContract(beanClass);
    }

    @Test
    public void testToString() {
        assertToString(beanClass);
    }
}
