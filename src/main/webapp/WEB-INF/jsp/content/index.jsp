<%@ include file="../include/include.jsp" %>

<span class="services">&nbsp;</span>
<ul class="services">
  <c:url value="services/ParaBank?wsdl" var="wsdl"/>
  <li class="captionone"><fmt:message key="atm.services"/></li>
  <li><a href="${wsdl}"><fmt:message key="withdraw.funds"/></a></li>
  <li><a href="${wsdl}"><fmt:message key="transfer.funds"/></a></li>
  <li><a href="${wsdl}"><fmt:message key="check.balances"/></a></li>
  <li><a href="${wsdl}"><fmt:message key="make.deposits"/></a></li>
</ul>
<ul class="servicestwo">
  <c:url value="services/bank?_wadl&_type=xml" var="wadl"/>
  <li class="captiontwo"><fmt:message key="online.services"/></li>
  <li><a href="<c:url value="${wadl}"/>"><fmt:message key="bill.pay"/></a></li>
  <li><a href="<c:url value="${wadl}"/>"><fmt:message key="account.history"/></a></li>
  <li><a href="<c:url value="${wadl}"/>"><fmt:message key="transfer.funds"/></a></li>
</ul>
<p class="more"><a href="<c:url value="services.htm"/>"><fmt:message key="read.more"/></a></p>
<h4><fmt:message key="latest.news"/></h4>
<ul class="events">
  <li class="captionthree"><fmt:formatDate pattern="MM/dd/yyyy" value="${model.date}"/></li>
  <c:forEach items="${model.news}" var="item">
    <li><a href="<c:url value="news.htm#${item.id}"/>">${item.headline}</a></li>
  </c:forEach>
</ul>
<p class="more"><a href="<c:url value="news.htm"/>"><fmt:message key="read.more"/></a></p>