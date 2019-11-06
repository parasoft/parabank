package com.parasoft.parabank.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.parasoft.parabank.domain.Account;


//This class was created for the sole purpose of displaying the list of transactions in RESTXML format.
@XmlRootElement(name="accounts")
public class Accounts {
    @XmlElement(name="account", type = Account.class)
    List<Account> accounts = new ArrayList<>();



    //getter method name changed from default to getAccs to avoid JAXB name conflict
    public List<Account> getAccs() {
        return accounts;
    }

    //setter method name changed from default to setAccs to avoid JAXB name conflict
    public void setAccs(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Accounts [accounts=" + accounts + "]";
    }






}
