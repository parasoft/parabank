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
    private ContactInformation contactInformation;
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

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
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
        result = prime * result + (contactInformation == null ? 0 : contactInformation.hashCode());
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
            Util.equals(contactInformation, other.contactInformation) &&
            Util.equals(accountNumber, other.accountNumber);
    }

    @Override
    public String toString() {
        return "Payee [name=" + name + ", address=" + address
                + ", contactInformation=" + contactInformation + ", accountNumber="
                + accountNumber + "]";
    }
}
