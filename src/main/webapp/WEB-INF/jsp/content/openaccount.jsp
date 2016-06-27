<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="open.new.account"/></h1>

<form:form method="post" commandName="openAccountForm">
  <p><b><fmt:message key="what.type.of.account"/></b></p>
  <div><form:select cssClass="input" path="type">
    <form:options items="${types}"/>
  </form:select>
  <form:errors path="type" cssClass="error"/></div>
  <br/>
  <fmt:formatNumber type="currency" value="${minimumBalance}" var="minValue"/>
  <p><b><fmt:message key="minimum.deposit"><fmt:param value="${minValue}"/></fmt:message></b></p>
  <div><form:select cssClass="input" path="fromAccountId">
    <form:options items="${accounts}"/>
  </form:select>
  <form:errors path="fromAccountId" cssClass="error"/></div>
  <br/>
  <div><input type="submit" class="button" value="<fmt:message key="open.new.account"/>"></div>
</form:form>