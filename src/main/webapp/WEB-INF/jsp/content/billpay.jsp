<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="bill.payment.service"/></h1>

<p><fmt:message key="enter.payee.information"/></p>

<form:form method="post" commandName="billPayForm">

<table class="form2" >
  <tr>
    <td align="right" width="30%"><b><fmt:message key="payee.name"/>:</b></td>
    <td width="20%"><form:input cssClass="input" path="payee.name"/></td>
    <td width="50%"><form:errors path="payee.name" cssClass="error"/></td>
  </tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="address"/>:</b></td>
    <td width="20%"><form:input cssClass="input" path="payee.address.street"/></td>
    <td width="50%"><form:errors path="payee.address.street" cssClass="error"/></td>
  </tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="city"/>:</b></td>
    <td width="20%"><form:input cssClass="input" path="payee.address.city"/></td>
    <td width="50%"><form:errors path="payee.address.city" cssClass="error"/></td>
  </tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="state"/>:</b></td>
    <td width="20%"><form:input cssClass="input" path="payee.address.state"/></td>
    <td width="50%"><form:errors path="payee.address.state" cssClass="error"/></td>
  </tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="zip.code"/>:</b></td>
    <td width="20%"><form:input cssClass="input" path="payee.address.zipCode"/></td>
    <td width="50%"><form:errors path="payee.address.zipCode" cssClass="error"/></td>
  </tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="phone.number"/>:</b></td>
    <td width="20%"><form:input cssClass="input" path="payee.phoneNumber"/></td>
    <td width="50%"><form:errors path="payee.phoneNumber" cssClass="error"/></td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="payee.account"/>:</b></td>
    <td><form:input cssClass="input" path="payee.accountNumber"/></td>
    <td width="50%"><form:errors path="payee.accountNumber" cssClass="error"/></td>
  </tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="verify.account"/>:</b></td>
    <td width="20%"><form:input cssClass="input" path="verifyAccount"/></td>
    <td width="50%"><form:errors path="verifyAccount" cssClass="error"/></td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td align="right" width="30%"><b><fmt:message key="billpay.amount"/>: $</b></td>
    <td width="20%"><form:input cssClass="input" path="amount"/></td>
    <td width="50%"><form:errors path="amount" cssClass="error"/></td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td align="right"><b><fmt:message key="from.account.number"/>:</b></td>
    <td>
      <form:select cssClass="input" path="fromAccountId">
        <form:options items="${accounts}"/>
      </form:select>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" class="button" value="<fmt:message key="send.payment"/>"></td>
  </tr>  
</table>

</form:form>