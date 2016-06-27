<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="signing.up"/></h1>

<p><fmt:message key="personal.info"/></p>

<form:form method="post" commandName="customerForm" >
  <table class="form2" >
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
      <td align="right" width="30%"><b><fmt:message key="ssn"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.ssn"/>
      </td>
      <td width="50%">
        <form:errors path="customer.ssn" cssClass="error"/>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="username"/>:</b></td>
      <td width="20%">
        <form:input cssClass="input" path="customer.username"/>
      </td>
      <td width="50%">
        <form:errors path="customer.username" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="password"/>:</b></td>
      <td width="20%">
        <form:password cssClass="input" path="customer.password"/>
      </td>
      <td width="50%">
        <form:errors path="customer.password" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="30%"><b><fmt:message key="repeated.password"/>:</b></td>
      <td width="20%">
        <form:password cssClass="input" path="repeatedPassword"/>
      </td>
      <td width="50%">
        <form:errors path="repeatedPassword" cssClass="error"/>
      </td>
    </tr>    
    <tr>
      <td>&nbsp;</td>
      <td colspan="2"><input type="submit" class="button" value="<fmt:message key="register"/>"></td>
    </tr>
  </table>
  <br>
</form:form>