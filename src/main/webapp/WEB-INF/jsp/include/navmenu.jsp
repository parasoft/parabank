<%@ include file="include.jsp" %>

<ul>
  <li><a href="<c:url value="/openaccount.htm"/>"><fmt:message key="open.new.account"/></a></li>
  <li><a href="<c:url value="/overview.htm"/>"><fmt:message key="accounts.overview"/></a></li>
  <li><a href="<c:url value="/transfer.htm"/>"><fmt:message key="transfer.funds"/></a></li>
  <li><a href="<c:url value="/billpay.htm"/>"><fmt:message key="bill.pay"/></a></li>
  <li><a href="<c:url value="/findtrans.htm"/>"><fmt:message key="find.transactions"/></a></li>
  <li><a href="<c:url value="/updateprofile.htm"/>"><fmt:message key="update.contact.info"/></a></li>
  <li><a href="<c:url value="/requestloan.htm"/>"><fmt:message key="request.loan"/></a></li>
  <li><a href="<c:url value="/logout.htm"/>"><fmt:message key="logout"/></a></li>
</ul>