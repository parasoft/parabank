package com.parasoft.parabank.domain;

import javax.xml.bind.annotation.XmlType;

import com.parasoft.parabank.util.Util;

/**
 * Domain object representing an address
 */
@XmlType(propOrder={"street", "city", "state", "zipCode"})
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (street == null ? 0 : street.hashCode());
        result = prime * result + (city == null ? 0 : city.hashCode());
        result = prime * result + (state == null ? 0 : state.hashCode());
        result = prime * result + (zipCode == null ? 0 : zipCode.hashCode());
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
        Address other = (Address)obj;
        return Util.equals(street, other.street) &&
            Util.equals(city, other.city) &&
            Util.equals(state, other.state) &&
            Util.equals(zipCode, other.zipCode);
    }

    @Override
    public String toString() {
        return "Address [street=" + street + ", city=" + city + ", state="
                + state + ", zipCode=" + zipCode + "]";
    }
}
