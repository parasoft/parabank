<%@ include file="include.jsp" %>

<div id="footermainPanel">
  <div id="footerPanel">
    <ul>
      <li><a href="<c:url value="/index.htm"/>"><fmt:message key="home"/></a>| </li>
      <li><a href="<c:url value="about.htm"/>"><fmt:message key="about.us"/></a>| </li>
      <li><a href="<c:url value="services.htm"/>"><fmt:message key="services"/></a>| </li>
      <li><a href="http://www.parasoft.com/jsp/products.jsp"><fmt:message key="products"/></a>| </li>
      <li><a href="http://www.parasoft.com/jsp/pr/contacts.jsp"><fmt:message key="locations"/></a>| </li>
      <li><a href="http://forums.parasoft.com/"><fmt:message key="forum"/></a>| </li>
      <li><a href="<c:url value="/sitemap.htm"/>"><fmt:message key="site.map"/></a>| </li>
      <li><a href="<c:url value="contact.htm"/>"><fmt:message key="contact.us"/></a></li>
    </ul>
    <p class="copyright">&copy; Parasoft. All rights reserved.</p>
    <ul class="visit">
      <li><fmt:message key="visit.us"/>:</li>
      <li><a href="http://www.parasoft.com/" target="_blank">www.parasoft.com</a></li>
    </ul>
  </div>
</div>