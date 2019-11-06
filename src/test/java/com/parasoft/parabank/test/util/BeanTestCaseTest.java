package com.parasoft.parabank.test.util;

import org.junit.Test;

import com.parasoft.parabank.util.Util;

/**
 * @req PAR-44
 *
 */
public class BeanTestCaseTest extends AbstractParaBankTest {
    @Test
    public void testEqualsContractMet() {
        AbstractBeanTestCase.assertMeetsEqualsContract(ChildObject.class);
        AbstractBeanTestCase.assertMeetsEqualsContract(TestObject.class);
    }

    @Test
    public void testHashCodeContractMet() {
        AbstractBeanTestCase.assertMeetsHashCodeContract(ChildObject.class);
        AbstractBeanTestCase.assertMeetsHashCodeContract(TestObject.class);
    }

    static final class TestObject {
        private String str;

        private boolean bool;

        private short shrt;

        private long lng;

        private float flt;

        private ChildObject co;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (str == null ? 0 : str.hashCode());
            result = prime * result + (bool ? 1231 : 1237);
            result = prime * result + shrt;
            result = prime * result + (int) (lng ^ lng >>> 32);
            result = prime * result + Float.floatToIntBits(flt);
            result = prime * result + (co == null ? 0 : co.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TestObject)) {
                return false;
            }
            TestObject other = (TestObject) obj;
            return Util.equals(str, other.str) && bool == other.bool && shrt == other.shrt && lng == other.lng
                && Float.floatToIntBits(flt) == Float.floatToIntBits(other.flt) && Util.equals(co, other.co);
        }
    }

    static final class ChildObject {
        private int i;

        private byte b;

        private char ch;

        private double dbl;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + i;
            result = prime * result + b;
            result = prime * result + ch;
            long temp;
            temp = Double.doubleToLongBits(dbl);
            result = prime * result + (int) (temp ^ temp >>> 32);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ChildObject)) {
                return false;
            }
            ChildObject other = (ChildObject) obj;
            return i == other.i && b == other.b && ch == other.ch
                && Double.doubleToLongBits(dbl) == Double.doubleToLongBits(other.dbl);
        }
    }
}
