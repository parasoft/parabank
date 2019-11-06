package com.parasoft.parabank.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.parasoft.parabank.domain.Transaction;


// This class was created for the sole purpose of displaying the list of transactions in RESTXML format.
@XmlRootElement(name="transactions")
public class Transactions {
    @XmlElement(name=Constants.TRANSACTION, type = Transaction.class)
    List<Transaction> transactions = new ArrayList<>();


    //getter method name changed from default to getTrans to avoid JAXB name conflict
    public List<Transaction> getTranss() {
        return transactions;
    }
    //setter method name changed from default to setTrans to avoid JAXB name conflict
    public void setTrans(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
