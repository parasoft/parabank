<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="error.heading"/></h1>

<fmt:message key="no.further.info" var="noInfo"/>
<fmt:message key="${model.message}" var="message">
  <c:forEach items="${model.parameters}" var="parameter">
    <fmt:param value="${parameter}"/>
  </c:forEach>
</fmt:message>
<p class="error"><c:out value="${message}" default="${noInfo}"/></p>