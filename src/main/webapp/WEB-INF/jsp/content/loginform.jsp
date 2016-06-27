<%@ include file="../include/include.jsp" %>

<c:if test="${!empty message}">
  <b><font color="RED"><c:url value="${message}"/></font></b>
</c:if>

<p class="error"><fmt:message key="error.log.in"/></p>

<br/>

<h1 class="title"><fmt:message key="sign.up"/></h1>
<a href="<c:url value="/register.htm"/>"><fmt:message key="enroll"/></a>

<br/>
<br/>

<h1 class="title"><fmt:message key="forgot.your.password"/></h1>
<a href="<c:url value="/lookup.htm"/>"><fmt:message key="verify.account.information"/></a>
