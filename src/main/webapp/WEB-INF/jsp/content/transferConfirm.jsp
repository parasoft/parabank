<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="transfer.complete"/></h1>

<p>
  <fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${model.amount}" var="amount"/>
  <fmt:formatNumber type="number" pattern="#" value="${model.fromAccountId}" var="fromAccountId"/>
  <fmt:formatNumber type="number" pattern="#" value="${model.toAccountId}" var="toAccountId"/>
  <fmt:message key="transfer.confirmation">
    <fmt:param value="<span id='amount'>${amount}</span>"/>
    <fmt:param value="<span id='fromAccountId'>${fromAccountId}</span>"/>
    <fmt:param value="<span id='toAccountId'>${toAccountId}</span>"/>
  </fmt:message>
</p>

<p><fmt:message key="see.account.activity"/></p>