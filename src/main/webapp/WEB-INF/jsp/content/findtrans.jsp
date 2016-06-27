<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="find.transactions"/></h1>

<form:form method="post" commandName="findTransactionForm">
  <div>
    <b><fmt:message key="select.an.account"/></b>:
    <form:select cssClass="input" path="accountId">
      <form:options items="${accounts}"/>
    </form:select>
    <form:errors path="accountId" cssClass="error"/>
  </div>
  
  <hr/>
  
  <div>
    <b><fmt:message key="find.by.transaction.id"/></b>: <form:input cssClass="input" path="criteria.transactionId"/>
    <form:errors path="criteria.transactionId" cssClass="error"/>
  </div>
  <br/>
  <div>
    <button type="submit" class="button" name="criteria.searchType" value="ID">
      <fmt:message key="find.transactions"/>
    </button>
  </div>
  
  <hr/>
  
  <div>
    <b><fmt:message key="find.by.date"/></b>: <form:input cssClass="input" path="criteria.onDate"/>
    (<fmt:message key="date.format"/>)
    <br/>
    <form:errors path="criteria.onDate" cssClass="error"/>
  </div>
  <br/>
  <div>
    <button type="submit" class="button" name="criteria.searchType" value="DATE">
      <fmt:message key="find.transactions"/>
    </button>
  </div>
  
  <hr/>
  
  <div>
    <p><b><fmt:message key="find.by.date.range"/></b></p>
    <div>
      <fmt:message key="between"/>
      <form:input cssClass="input" path="criteria.fromDate"/>
      <fmt:message key="and"/>
      <form:input cssClass="input" path="criteria.toDate"/>
      (<fmt:message key="date.format"/>)
    </div>
    <div>
      <form:errors path="criteria.fromDate" cssClass="error"/>
      <br/>
      <form:errors path="criteria.toDate" cssClass="error"/>
    </div>
  </div>
  <br/>
  <div>
    <button type="submit" class="button" name="criteria.searchType" value="DATE_RANGE">
      <fmt:message key="find.transactions"/>
    </button>
  </div>

  <hr/>
  
  <div>
    <b><fmt:message key="find.by.amount"/></b>: <form:input cssClass="input" path="criteria.amount"/>
    <form:errors path="criteria.amount" cssClass="error"/>
  </div>
  <br/>
  <div>
    <button type="submit" class="button" name="criteria.searchType" value="AMOUNT">
      <fmt:message key="find.transactions"/>
    </button>
  </div>
  
</form:form>