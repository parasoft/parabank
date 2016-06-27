<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="account.details"/></h1>

<table>
  <tr>
    <td align="right"><fmt:message key="account.number"/>:</td>
    <td id="accountId">${model.account.id}</td>
  </tr>
  <tr>
    <td align="right"><fmt:message key="account.type"/>:</td>
    <td id="accountType">${model.account.type}</td>
  </tr>
  <tr>
    <td align="right"><fmt:message key="account.balance"/>:</td>
    <td id="balance"><fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${model.account.balance}"/></td>
  </tr>
  <tr>
    <td align="right"><fmt:message key="account.available.balance"/>:</td>
    <td id="availableBalance"><fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${model.account.availableBalance}"/></td>
  </tr>
</table>

<br/>

<h1 class="title"><fmt:message key="account.activity"/></h1>

<form:form method="post" commandName="transactionCriteria">
<table class="form_activity">
  <tr>
    <td align="right"><b><fmt:message key="activity.period"/>:</b></td>
    <td>
      <form:select cssClass="input" path="month">
        <form:options items="${months}"/>
      </form:select>
    </td>
  </tr>
  <tr>
    <td align="right"><b><fmt:message key="transaction.type"/>:</b></td>
    <td>
      <form:select cssClass="input" path="transactionType">
        <form:options items="${types}"/>
      </form:select>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" class="button" value="Go"></td>
  </tr>
</table>
</form:form>

<br/>

<jsp:include page="transactions.jsp"/>