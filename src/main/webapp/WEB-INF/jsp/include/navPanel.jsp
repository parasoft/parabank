<%@ include file="include.jsp" %>

<p class="smallText"><b><fmt:message key="welcome"/></b> ${userSession.customer.fullName}</p>

<h2><fmt:message key="account.services"/></h2>
<jsp:include page="navmenu.jsp"/>