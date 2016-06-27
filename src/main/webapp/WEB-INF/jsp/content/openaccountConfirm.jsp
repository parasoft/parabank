<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="account.opened"/></h1>

<c:url var="accountUrl" value="/activity.htm">
  <c:param name="id" value="${account.id}"/>
</c:url>

<p><fmt:message key="congratulations"/></p>
<p><b><fmt:message key="new.account.number"/>:</b> <a id="newAccountId" href="${accountUrl}">${account.id}</a></p>