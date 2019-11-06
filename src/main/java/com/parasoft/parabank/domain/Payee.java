package com.parasoft.parabank.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.parasoft.parabank.util.Util;

/**
 * Domain object representing the recipient of a bill payment
 */
@XmlRootElement
public class Payee {
    private String name;
    private Address address;
    private String phoneNumber;
    private Integer accountNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (address == null ? 0 : address.hashCode());
        result = prime * result + (phoneNumber == null ? 0 : phoneNumber.hashCode());
        result = prime * result + (accountNumber == null ? 0 : accountNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Payee)) {
            return false;
        }
        Payee other = (Payee) obj;
        return Util.equals(name, other.name) &&
            Util.equals(address, other.address) &&
            Util.equals(phoneNumber, other.phoneNumber) &&
            Util.equals(accountNumber, other.accountNumber);
    }

    @Override
    public String toString() {
        return "Payee [name=" + name + ", address=" + address
                + ", phoneNumber=" + phoneNumber + ", accountNumber="
                + accountNumber + "]";
    }
}
