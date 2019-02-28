/*
 * (C) Copyright Parasoft Corporation 2019.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.parabank.domain;

import com.parasoft.parabank.util.Util;

public class ContactInformation {
    private String phoneNumber;
    private String email;

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (email == null ? 0 : email.hashCode());
        result = prime * result + (phoneNumber == null ? 0 : phoneNumber.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Address)) {
            return false;
        }
        ContactInformation other = (ContactInformation)obj;
        return Util.equals(phoneNumber, other.phoneNumber) &&
            Util.equals(email, other.email);
    }

    @Override
    public String toString() {
        return "ContactInformation [phoneNumber=" + phoneNumber + ", email=" + email + "]";
    }
}
