<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="customer.lookup"/></h1>

<p><fmt:message key="located.successfully"/></p>

<p>
  <b><fmt:message key="username"/></b>: ${customer.username}
  <br/>
  <b><fmt:message key="password"/></b>: ${customer.password}
</p>