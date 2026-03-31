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
		<title>JumiBank | <fmt:message key="${view}.title" /></title>
		<link rel="icon" href="<c:url value="images/jumibank-icon.svg"/>" type="image/svg+xml" />
		<link href="<c:url value="theme.css"/>" rel="stylesheet" type="text/css" />
		<link href="<c:url value="template.css"/>" rel="stylesheet"
			type="text/css" />
		<link href="<c:url value="style.css"/>" rel="stylesheet"
			type="text/css" />
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" integrity="sha512-Avb2QiuDEEvB4bZJYdft2mNjVShBftLdPG8FJ0V7irTLQ8Uu0qcPxh4Plq7G5tGm0rU+1SPhVotteLpBERwTkw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
		<script src="<c:url value="webjars/jquery/3.7.1/jquery.min.js" />"></script>
	</head>

	<body
		<c:if test="${empty userSession.customer}">
	    onload="document.login.username.focus();"
	  </c:if>>
		<%@ include file="include/appGlobals.jsp" %>
		<script src="<c:url value="js/jumibank-core.js"/>"></script>
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
