<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link type="text/css" rel="stylesheet"
 href="/parabank/services/?stylesheet=1">
  <meta http-equiv="content-type"
 content="text/html; charset=UTF-8">
  <title>CXF - Service list</title>
</head>
<body>
<%
   String hostname = request.getServerName();
   int port = request.getServerPort();
%>
<span class="heading">Available Bookstore SOAP services:</span><br>
<table style="text-align: left; width: 925px;" border="1"
 cellpadding="1" cellspacing="1" width="100%">
  <tbody>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">Bookstore</span>
      <ul>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Parasoft
Bookstore Web service with a database backend. Includes a total of 9
book items.<br>
      <br>
Endpoint address:</span> <span class="value">http://<%= hostname%>:<%= port %>/parabank/services/store-01</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= request.getServerName() %>:<%= port%>/parabank/services/store-01?wsdl">{http://store.parabank.parasoft.com/}Bookstore</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://store.parabank.parasoft.com/</span></td>
    </tr>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">Bookstore (Version 2.0)</span>
      <ul>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Parasoft
Bookstore Web service with a database backend. Includes a total of 9
book items.<br>
      <br>
Endpoint address:</span> <span class="value">http://<%= hostname%>:<%= port %>/parabank/services/store-01V2</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= request.getServerName() %>:<%= port%>/parabank/services/store-01V2?wsdl">{http://store.parabank.parasoft.com/}Bookstore</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://store.parabank.parasoft.com/</span></td>
    </tr>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">Bookstore <br>
(WS-Security Username Token)</span>
      <ul>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Parasoft
Bookstore Web service secured using WS-Security Username Token. <br>
Clients
need to authenticate themselves using Username Token.<br>
      <br>
username:soatest<br>
password:soatest<br>
      <br>
Endpoint address:</span> <span class="value">http://<%= hostname%>:<%= port%>/parabank/services/store-wss-01</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= hostname%>:<%= port%>/parabank/services/store-wss-01?wsdl">{http://store.parabank.parasoft.com/}Bookstore</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://store.parabank.parasoft.com/</span></td>
    </tr>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">Bookstore <br>
(WS-Security Signature)</span>
      <ul>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Parasoft
Bookstore Web service secured using WS-Security Signature. <br>
Clients need
to sign the SOAP Body using the key store <a href="soatest.pfx">soatest.pfx</a><br>
      <br>
key store password: security<br>
certificate alias: soatest<br>
private key alias: soatest<br>
algorithm: RSA<br>
      <br>
Responses have the SOAP Body signed using the same certificate.<br>
      <br>
Endpoint address:</span> <span class="value">http://<%= hostname%>:<%= port%>/parabank/services/store-wss-02</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= hostname%>:<%= port%>/parabank/services/store-wss-02?wsdl">{http://store.parabank.parasoft.com/}Bookstore</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://store.parabank.parasoft.com/</span></td>
    </tr>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">Bookstore <br>
(WS-Security Encryption)</span>
      <ul>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Parasoft
Bookstore Web service secured using WS-Security Encryption. <br>
Clients
need to encrypt the SOAP Body using the key store <a href="soatest.pfx">soatest.pfx</a><br>
      <br>
key store password: security<br>
public key alias: soatest<br>
      <br>
Responses have the SOAP Body encrypted using the same key.<br>
      <br>
Endpoint address:</span> <span class="value">http://<%= hostname%>:<%= port%>/parabank/services/store-wss-03</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= hostname%>:<%= port%>/parabank/services/store-wss-03?wsdl">{http://store.parabank.parasoft.com/}Bookstore</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://store.parabank.parasoft.com/</span></td>
    </tr>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">Bookstore <br>
(WS-Security Signature and
Encryption)</span>
      <ul>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Parasoft
Bookstore Web service secured using WS-Security Signature and
Encryption. <br>
Clients need to sign, then encrypt the SOAP Body using the
key store <a href="soatest.pfx">soatest.pfx</a><br>
      <br>
key store password: security<br>
key alias: soatest<br>
      <br>
Responses have the SOAP Body signed, then encrypted using the same key<br>
      <br>
Endpoint address:</span> <span class="value">http://<%= hostname%>:<%= port%>/parabank/services/store-wss-04</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= hostname%>:<%= port%>/parabank/services/store-wss-04?wsdl">{http://store.parabank.parasoft.com/}Bookstore</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://store.parabank.parasoft.com/</span></td>
    </tr>
  </tbody>
</table>
<br>
<br>
<span class="heading">Bookstore services:</span><br>
<table style="text-align: left; width: 925px;" border="1"
 cellpadding="1" cellspacing="1" width="100%">
  <tbody>
    <tr>
      <td colspan="1" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 204, 204);">Method<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 204, 204); width: 203px;">Parameters<br>
      </td>
      <td rowspan="1" colspan="4"
 style="vertical-align: top; background-color: rgb(204, 204, 204);">Description<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">getItemById</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int</td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Returns
the Book object with the given item id. Valid id values are <b>1</b><span
 style="font-weight: bold;">- 9</span>, or if you add a
new entry using the <span style="font-style: italic;">addNewItemToInventory</span>
method,&nbsp;you can
retrieve a Book using the new id.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">getItemByTitle </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">string</td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Returns
a list of Book objects that match your title search query. The Book's
price value will increase by $1.00
every 5 invocations.<br>
Example keywords: <b>linux</b>, <b>java</b>, <b>C++</b>,
      <b>program</b>.
&nbsp;<font color="#990000">Leave it blank to get ALL
the books in the
database</font>.</td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">addItemToCart<font
 color="#000099"> </font></td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int,
int, int<font color="#000099"><br>
      </font></td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Adds
a specified amount of Books to a cart. If no cartId is given or if the
cartId is equal to 0, a unique Id will be genearted for the user.<br>
All carts that are inactive for 20 minutes will be removed.</td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">getItemsInCart<font
 color="#000099"> </font></td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Returns a list of items that
exist in the specified cart.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">updateItemInCart</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int,
int, int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Update
an existing order in a cart. Cannot update an order where quantity
supplied is greater than the quantity in stock. Possible to update na
order to have zero quantity.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">submitOrder</td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Submits the items in a cart.
This removes all the existing items in the cart and deletes the cartId.</td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">addNewItemToInventory</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">Book</td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Add
new book entries into the database. All new entries that are not
accessed within 20 minutes will be removed from the database.</td>
    </tr>
  </tbody>
</table>
<br>
<br>
<span class="heading">Available Parabank SOAP services:</span><br>
<table style="text-align: left; width: 925px;" border="1"
 cellpadding="1" cellspacing="1" width="100%">
  <tbody>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">LoanProcessorService</span>
      <ul>
        <li>requestLoan</li>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Endpoint
address:</span> <span class="value">http://<%= hostname%>:<%= port%>/parabank/services/LoanProcessor</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= hostname%>:<%= port%>/parabank/services/LoanProcessor?wsdl">{http://service.parabank.parasoft.com/}LoanProcessorServiceImplService</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://service.parabank.parasoft.com/</span></td>
    </tr>
    <tr>
      <td style="width: 400px;"><span
 class="porttypename">ParaBankService</span>
      <ul>
      </ul>
      </td>
      <td style="width: 1183px;"><span class="field">Endpoint
address:</span> <span class="value">http://<%= hostname%>:<%= port%>/parabank/services/ParaBank</span><br>
      <span class="field">WSDL :</span> <a
 href="http://<%= hostname%>:<%= port%>/parabank/services/ParaBank?wsdl">{http://service.parabank.parasoft.com/}ParaBank</a><br>
      <span class="field">Target namespace:</span> <span
 class="value">http://service.parabank.parasoft.com/</span></td>
    </tr>
  </tbody>
</table>
<br>
<br>
<span class="heading">Parabank services:</span><br>
<table style="text-align: left; width: 925px;" border="1"
 cellpadding="1" cellspacing="1" width="100%">
  <tbody>
    <tr>
      <td colspan="1" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 204, 204);">Method<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 204, 204); width: 203px;">Parameters<br>
      </td>
      <td rowspan="1" colspan="4"
 style="vertical-align: top; background-color: rgb(204, 204, 204);">Description<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">getAccount</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int</td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Returns
account information for a given account number.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">buyPosition </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int, int,
string, string, int, double</td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Buy a position.</td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">deposit</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int,
double<font color="#000099"><br>
      </font></td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Deposit
funds into the given account.</td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">startupJmsListener</td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;"><font
 color="#000099"><br>
      </font></td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Enable JMS message listener.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">shutdownJmsListener</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;"><br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Disable
JMS message listener.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">sellPosition</td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int, int, int,
int, double<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Sell a position.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">login</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">string,
string<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Return
the customer id for the given username and password.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">getCustomer</td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Return customer information for
the given customer number.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">getAccounts</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Return
a list of accounts for a given customer.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">setParameter<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">string, string<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Sets the value of a given
configuration parameter.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">getPositionHistory</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int,
Date, Date<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Return
position history for a given position id and date range.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">requestLoan<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int, double,
double, int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Request a loan.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">cleanDB</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;"><br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Reset
database contents to a minimal state.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">withdraw<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int, double<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Withdraw funds out of the given
account.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">getPosition</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Return
a position for a given position number.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">initializeDB<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;"><br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Reset database contents to a
populated state<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">getTransaction</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Return
transaction information for a given transaction id.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">getPositions<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Return a list of positions for
a given customer.<br>
      </td>
    </tr>
    <tr>
      <td
 style="background-color: rgb(204, 255, 255); vertical-align: middle;">getTransactions</td>
      <td colspan="2" rowspan="1"
 style="background-color: rgb(204, 255, 255); vertical-align: middle; width: 203px;">int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Return
a list of transactions for a given account.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">transfer<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int, int,
double<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Transfer funds between two
accounts.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle; background-color: rgb(204, 255, 255);">createAccount<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px; background-color: rgb(204, 255, 255);">int, int, int<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Create a new account.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">getTransactionsOnDate<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int, string<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Retrieve an account's transactions for a particular date.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle; background-color: rgb(204, 255, 255);">getTransactionsByToFromDate<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px; background-color: rgb(204, 255, 255);">int, string, string<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Retrieve an account's transactions between a range of dates.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle;">getTransactionsByAmount<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px;">int, double<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top;">Retrieve an account's transactions matching the given monetary amount.<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle; background-color: rgb(204, 255, 255);">updateCustomer<br>
      </td>
      <td colspan="2" rowspan="1"
 style="vertical-align: middle; width: 203px; background-color: rgb(204, 255, 255);">int, string, string, string, string, string, string, string, 
string, string, string<br>
      </td>
      <td colspan="4" rowspan="1"
 style="vertical-align: top; background-color: rgb(204, 255, 255);">Update a customer's information.<br>
      </td>
    </tr>
  </tbody>
</table>
<br>
<br>
<span class="heading">Available RESTful services:</span><br>
<table style="text-align: left; width: 925px;" border="1"
 cellpadding="1" cellspacing="1" width="100%">
  <tbody>
    <tr>
      <td><span class="field">Endpoint address:</span>
      <span class="value">http://<%= hostname%>:<%= port%>/parabank/services/bank</span><br>
      <span class="field">WADL :</span> <a
 href="http://<%= hostname%>:<%= port%>/parabank/services/bank?_wadl&amp;_type=xml">http://<%= hostname%>:<%= port%>/parabank/services/bank?_wadl&amp;type=xml</a></td>
    </tr>
  </tbody>
</table>
</body>
</html>
