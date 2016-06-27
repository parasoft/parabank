<%@ include file="../include/include.jsp" %>

<c:choose>
<c:when test="${empty model.transactions}">
  <p><b><fmt:message key="no.transactions.found"/></b></p>
</c:when>
<c:otherwise>
<table id="transactionTable" class="gradient-style">
  <thead>
    <tr>
      <th><fmt:message key="date"/></th>
      <th><fmt:message key="transaction"/></th>
      <th><fmt:message key="debit"/></th>
      <th><fmt:message key="credit"/></th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${model.transactions}" var="transaction">
    <tr>
      <td><fmt:formatDate pattern="MM-dd-yyyy" value="${transaction.date}"/></td>
      <td><a href="<c:url value="/transaction.htm?id=${transaction.id}"/>">${transaction.description}</a></td>
      <td>
        <c:if test="${transaction.type eq 'Debit'}">
          <fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${transaction.amount}"/>
        </c:if>
      </td>
      <td>
        <c:if test="${transaction.type eq 'Credit'}">
          <fmt:formatNumber type="currency" pattern="$0.00;-$0.00" value="${transaction.amount}"/>
        </c:if>
      </td>
    </tr>
    </c:forEach>
  </tbody>
</table>
</c:otherwise>
</c:choose>