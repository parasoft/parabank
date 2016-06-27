<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="parabank.news"/></h1>

<c:forEach items="${model.news}" var="entry">
  <h3><b><fmt:formatDate pattern="MM/dd/yyyy" value="${entry.key}"/></b></h3>
  <br/>
  <c:forEach items="${entry.value}" var="news">
    <p><b><a class="headline"  id="${news.id}">${news.headline}</a></b></p>
    <p>${news.story}</p>
  </c:forEach>
</c:forEach>