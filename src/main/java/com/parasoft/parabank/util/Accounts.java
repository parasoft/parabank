package com.parasoft.parabank.util;

import java.util.*;

import javax.xml.bind.annotation.*;

import com.parasoft.parabank.domain.*;


//This class was created for the sole purpose of displaying the list of transactions in RESTXML format.
@XmlRootElement(name="accounts")
public class Accounts {
	@XmlElement(name="account", type = Account.class)
	List<Account> accounts = new ArrayList<Account>();



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
