<%@page import="java.util.Enumeration"%>
<%@ include file="include/include.jsp"%>

<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 -->
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>ParaBank | <fmt:message key="${view}.title" /></title>
		<link href="<c:url value="/template.css"/>" rel="stylesheet"
			type="text/css" />
		<link href="<c:url value="/style.css"/>" rel="stylesheet"
			type="text/css" />
		<script src="<c:url value="/webjars/angularjs/1.8.2/angular.min.js"/>"></script>
	</head>

	<body
		<c:if test="${empty userSession.customer}">
	    onload="document.login.username.focus();"
	  </c:if>>
		<div id="mainPanel">
			<c:choose>
				<c:when test="${empty userSession.customer}">
					<jsp:include page="include/headerPanel.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include page="include/headerPanelCustomer.jsp" />
				</c:otherwise>
			</c:choose>
			<div id="bodyPanel">
				<div id="leftPanel">
					<c:choose>
						<c:when test="${empty userSession.customer}">
							<jsp:include page="include/loginPanel.jsp" />
						</c:when>
						<c:otherwise>
							<jsp:include page="include/navPanel.jsp" />
						</c:otherwise>
					</c:choose>
				</div>
				<div id="rightPanel">
					<jsp:include page="content/${view}.jsp" />
				</div>
			</div>
		</div>
		<jsp:include page="include/footerPanel.jsp" />
	</body>
</html>
