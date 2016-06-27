<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="customer.care"/></h1>

<p><fmt:message key="email.support.is.available"/></p>

<form:form method="post" commandName="contactForm">
  <table class="form2" >
    <tr>
      <td align="right" width="30%"><b><fmt:message key="name"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="name"/>
      </td>
      <td width="50%">
        <form:errors path="name" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="email"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="email"/>
      </td>
      <td width="50%">
        <form:errors path="email" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="phone"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="phone"/>
      </td>
      <td width="50%">
        <form:errors path="phone" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="message"/>:</b></td>
      <td width="20%">
        <form:textarea cols="85" rows="9" cssClass="input" path="message"/>
      </td>
      <td width="50%">
        <form:errors path="message" cssClass="error"/>
      </td>
    </tr>    
    <tr>
      <td>&nbsp;</td>
      <td colspan="2"><input type="submit" class="button" value="<fmt:message key="send.to.customer.care"/>"></td>
    </tr>
  </table>
  <br>
</form:form>