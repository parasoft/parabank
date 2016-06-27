<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="transaction.details"/></h1>

<table>
  <tr>
    <td align="right"><b><fmt:message key="transaction.id"/>:</b></td>
    <td>${transaction.id}</td>
  </tr>
  <tr>
    <td align="right"><b><fmt:message key="transaction.date"/>:</b></td>
    <td><fmt:formatDate pattern="MM-dd-yyyy" value="${transaction.date}"/></td>
  </tr>
  <tr>
    <td align="right"><b><fmt:message key="transaction.description"/>:</b></td>
    <td>${transaction.description}</td>
  </tr>
  <tr>
    <td align="right"><b><fmt:message key="transaction.type"/>:</b></td>
    <td>${transaction.type}</td>
  </tr>
  <tr>
    <td align="right"><b><fmt:message key="transaction.amount"/>:</b></td>
    <td><fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${transaction.amount}"/></td>
  </tr>  
</table>