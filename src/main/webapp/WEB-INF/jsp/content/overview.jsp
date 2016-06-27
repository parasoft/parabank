<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="accounts.overview"/></h1>

<table id="accountTable" class="gradient-style">
  <thead>
    <tr>
      <th><fmt:message key="account"/></th>
      <th><fmt:message key="balance"/></th>
      <th><fmt:message key="available.amount"/></th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${model.accounts}" var="account">
    <tr>
      <td><a href="<c:url value="/activity.htm?id=${account.id}"/>"><c:out value="${account.id}"/></a></td>
      <td><fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${account.balance}"/></td>
      <td><fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${account.availableBalance}"/></td>
    </tr>
    </c:forEach>  
    <tr>
      <td align="right"><b><fmt:message key="total"/></b></td>
      <td><b><fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${model.totalBalance}"/></b></td>
      <td>&nbsp;</td>
    </tr>
  </tbody>
  <tfoot>
    <tr>
      <td colspan="3"><fmt:message key="balance.note"/></td>
    </tr>
  </tfoot>    
</table>