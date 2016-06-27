<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="update.profile"/></h1>

<form:form method="post" commandName="customerFormUpdate" >
  <table class="form2">
    <tr>
      <td align="right" width="30%"><b><fmt:message key="first.name"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.firstName"/>
      </td>
      <td width="50%">
        <form:errors path="customer.firstName" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="last.name"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.lastName"/>
      </td>
      <td width="50%">
        <form:errors path="customer.lastName" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="address"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.address.street"/>
      </td>
      <td width="50%">
        <form:errors path="customer.address.street" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="city"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.address.city"/>
      </td>
      <td width="50%">
        <form:errors path="customer.address.city" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="state"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.address.state"/>
      </td>
      <td width="50%">
        <form:errors path="customer.address.state" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="zip.code"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.address.zipCode"/>
      </td>
      <td width="50%">
        <form:errors path="customer.address.zipCode" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="phone.number"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.phoneNumber"/>
      </td>
      <td width="50%">
        <form:errors path="customer.phoneNumber" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td colspan="2"><input type="submit" class="button" value="<fmt:message key="update.profile"/>"></td>
    </tr>
  </table>
  <br>
  <form:hidden cssClass="input" path="repeatedPassword" />
  
</form:form>