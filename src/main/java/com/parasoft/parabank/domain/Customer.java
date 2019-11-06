package com.parasoft.parabank.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parasoft.parabank.util.Util;

/**
 * Domain object representing a bank customer
 */

@XmlRootElement(name="customer")
@XmlType(propOrder={"id", "firstName", "lastName", "address", "phoneNumber", "ssn"})

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private Address address;
    private String phoneNumber;
    private String ssn;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getFullName() {
        return firstName + " " + lastName;
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

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    @XmlTransient
    @JsonIgnore
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlTransient
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + (firstName == null ? 0 : firstName.hashCode());
        result = prime * result + (lastName == null ? 0 : lastName.hashCode());
        result = prime * result + (address == null ? 0 : address.hashCode());
        result = prime * result + (phoneNumber == null ? 0 : phoneNumber.hashCode());
        result = prime * result + (ssn == null ? 0 : ssn.hashCode());
        result = prime * result + (password == null ? 0 : password.hashCode());
        result = prime * result + (username == null ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) obj;
        return id == other.id &&
            Util.equals(firstName, other.firstName) &&
            Util.equals(lastName, other.lastName) &&
            Util.equals(address, other.address) &&
            Util.equals(phoneNumber, other.phoneNumber) &&
            Util.equals(ssn, other.ssn) &&
            Util.equals(username, other.username) &&
            Util.equals(password, other.password);
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", firstName=" + firstName
                + ", lastName=" + lastName + ", address=" + address
                + ", phoneNumber=" + phoneNumber + ", ssn=" + ssn
                + ", username=" + username + ", password=" + password + "]";
    }
}
