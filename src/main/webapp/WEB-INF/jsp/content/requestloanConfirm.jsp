<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="loan.request.processed"/></h1>

<table class="form" style="width: 500px;">
  <tr>
    <td align="right" width="25%"><b><fmt:message key="loan.provider.name"/>:</b></td>
    <td id="loanProviderName" width="75%">${loanResponse.loanProviderName}</td>
  </tr>
  <tr>
    <td align="right"><b><fmt:message key="loan.response.date"/>:</b></td>
    <td id="responseDate"><fmt:formatDate pattern="MM-dd-yyyy" value="${loanResponse.responseDate}"/></td>
  </tr>
  <tr>
    <td align="right"><b><fmt:message key="loan.status"/>:</b></td>
    <td id="loanStatus"><fmt:message key="${loanResponse.approved ? 'loan.approved' : 'loan.denied'}"/></td>
  </tr>
</table>

<br/>

<c:choose>
  <c:when test="${not loanResponse.approved}">
    <p class="error"><fmt:message key="${loanResponse.message}"/></p>
  </c:when>
  <c:otherwise>
    <c:url var="accountUrl" value="/activity.htm">
      <c:param name="id" value="${loanResponse.accountId}"/>
    </c:url>
    <p><fmt:message key="loan.approved.message"/></p>    
    <p><b><fmt:message key="new.account.number"/>:</b> <a id="newAccountId" href="${accountUrl}">${loanResponse.accountId}</a></p>
  </c:otherwise>
</c:choose>