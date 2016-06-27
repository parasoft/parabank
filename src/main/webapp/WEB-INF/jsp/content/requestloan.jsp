<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1 class="title"><fmt:message key="apply.for.a.loan"/></h1>

<form:form method="post" commandName="requestLoanForm">
  <table class="form2" >
    <tr>
      <td align="right" width="40%"><b><fmt:message key="loan.amount"/>:</b> $</td>
      <td width="20%">
        <form:input cssClass="input" path="amount"/>
      </td>
      <td width="40%">
        <form:errors path="amount" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="40%"><b><fmt:message key="down.payment"/>:</b> $</td>
      <td width="20%">
        <form:input cssClass="input" path="downPayment"/>
      </td>
      <td width="40%">
        <form:errors path="downPayment" cssClass="error"/>
      </td>
    </tr>
    <tr>
      <td align="right" width="40%"><b><fmt:message key="from.account.number"/>:</b></td>
      <td width="20%">
        <form:select cssClass="input" path="fromAccountId">
          <form:options items="${accounts}"/>
        </form:select>
      </td>
      <td width="40%">
        <form:errors path="fromAccountId" cssClass="error"/>
      </td>
    </tr>    
    <tr>
      <td>&nbsp;</td>
      <td colspan="2"><input type="submit" class="button" value="<fmt:message key="apply.now"/>"></td>
    </tr>
  </table>
  <br>
</form:form>