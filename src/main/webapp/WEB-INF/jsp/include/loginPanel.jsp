<%@ include file="include.jsp" %>

<h2><fmt:message key="customer.login"/></h2>
<div id="loginPanel">
  <form name="login" action="<c:url value="/login.htm"/>" method="POST">
    <c:if test="${!empty loginForwardAction}">
        <input type="hidden" name="forwardAction" value="<c:url value="${loginForwardAction}"/>"/>
    </c:if>
    <p><b><fmt:message key="username"/></b></p>
    <div class="login"><input type="text" class="input" name="username"/></div>
    <p><b><fmt:message key="password"/></b></p>
    <div class="login"><input type="password" class="input" name="password"/></div>
    <div class="login"><input type="submit" class="button" value="Log In"/></div>
  </form>
  <p><a href="<c:url value="lookup.htm"/>"><fmt:message key="forgot.login.info"/></a></p>
  <p><a href="<c:url value="register.htm"/>"><fmt:message key="register"/></a></p>
</div>