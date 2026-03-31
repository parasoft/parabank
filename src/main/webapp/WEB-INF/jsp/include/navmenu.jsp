<%@ include file="include.jsp" %>

<ul>
  <li><a href="<c:url value="openaccount.htm"/>"><i class="fas fa-plus"></i><fmt:message key="open.new.account"/></a></li>
  <li><a href="<c:url value="overview.htm"/>"><i class="fas fa-wallet"></i><fmt:message key="accounts.overview"/></a></li>
  <li><a href="<c:url value="transfer.htm"/>"><i class="fas fa-right-left"></i><fmt:message key="transfer.funds"/></a></li>
  <li><a href="<c:url value="billpay.htm"/>"><i class="fas fa-file-invoice-dollar"></i><fmt:message key="bill.pay"/></a></li>
  <li><a href="<c:url value="findtrans.htm"/>"><i class="fas fa-magnifying-glass"></i><fmt:message key="find.transactions"/></a></li>
  <li><a href="<c:url value="updateprofile.htm"/>"><i class="fas fa-user-pen"></i><fmt:message key="update.contact.info"/></a></li>
  <li><a href="<c:url value="requestloan.htm"/>"><i class="fas fa-hand-holding-dollar"></i><fmt:message key="request.loan"/></a></li>
  <li><a href="<c:url value="logout.htm"/>"><i class="fas fa-right-from-bracket"></i><fmt:message key="logout"/></a></li>
</ul>