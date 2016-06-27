<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="transfer.funds"/></h1>

<form:form method="post" commandName="transferForm">

<p><form:errors path="amount" cssClass="error"/></p>

<p><b><fmt:message key="transfer.amount"/>:</b> $<form:input cssClass="input" path="amount"/></p>
<div>
<fmt:message key="from.account.number"/>
<form:select cssClass="input" path="fromAccountId">
  <form:options items="${accounts}"/>
</form:select>
<fmt:message key="to.account.number"/>
<form:select cssClass="input" path="toAccountId">
  <form:options items="${accounts}"/>
</form:select>
</div>
<br/>
<div><input type="submit" class="button" value="<fmt:message key="transfer"/>"></div>

</form:form>