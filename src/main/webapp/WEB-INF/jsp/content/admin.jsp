<%@ include file="../include/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1 class="title">
	<fmt:message key="administration" />
</h1>
<c:choose>
	<c:when test="${not empty error}">
		<p style="color:maroon;">
			<b><fmt:message key="${error}" /></b>
		</p>
	</c:when>
	<c:when test="${not empty message}">
		<p style="color: #080">
			<b><fmt:message key="${message}" /></b>
		</p>
	</c:when>
</c:choose>
<%-- 
<c:if test="${not empty message}">
	<p style="color: #080">
		<b><fmt:message key="${message}" /></b>
	</p>
</c:if>
 --%>
<table class="table_hidden" style="width: 100%" >
  <tr>
    <td width="50%" >
    	<h3>
			<fmt:message key="database" />
		</h3>
		<form name="initializeDB" action="<c:url value="/db.htm"/>"
			method="POST">
			<table class="form2" style="width: 100%;">
				<tr>
					<td align="center">
						<button type="submit" class="button" name="action" value="INIT">
							<fmt:message key="database.initialize" />
						</button>
					</td>
					<td align="center">
						<button type="submit" class="button" name="action" value="CLEAN">
							<fmt:message key="database.clean" />
						</button>
				</tr>
			</table>
		</form>
	</td>
    <td width="50%" >
		<h3>
			<fmt:message key="jms.service" />
		</h3>
		<form name="toggleJms" action="<c:url value="/jms.htm"/>" method="POST">
			<input type="hidden" name="shutdown" value="${isJmsRunning}" />
			<table class="form2" style="width: 100%;" >
				<tr>
					<td align="right" width="30%"><b><fmt:message
								key="jms.service.status" />:</b></td>
					<td width="20%"><fmt:message
							key="${isJmsRunning ? 'jms.running' : 'jms.stopped'}" /></td>
					<td width="50%"><input type="submit" class="button"
						value="<fmt:message key="${isJmsRunning ? 'jms.shutdown' : 'jms.startup'}"/>" /></td>
				</tr>
			</table>
		</form>
    </td>
  </tr>
</table>
<br />

<form:form method="post" action="admin.htm" 
 commandName="adminForm"  >

	<h3>
		<fmt:message key="data.access.mode" />
	</h3>
			
	<table class="form" style="width: 100%">
		<tr>
			<td colspan="1" align="center" width="50%">
			  <form:radiobutton cssClass="input"
					path="accessMode" value ="soap"/><br/>SOAP<br/>&nbsp;
			</td>
			<td colspan="1" align="center" width="50%">
			  <form:radiobutton cssClass="input"
					path="accessMode" value = "restxml" /><br/>REST&nbsp;(XML)<br/>&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="1" align="center" width="50%">
			  <form:radiobutton cssClass="input"
					path="accessMode" value = "restjson"/><br/>REST&nbsp;(JSON)</td>
			<td colspan="1" align="center" width="50%">
			  <form:radiobutton cssClass="input"
					path="accessMode" value="jdbc" /><br/>JDBC*</td>
		</tr>
		<tr> <td colspan="2" align="left" width="100%"><hr/>* - Default <fmt:message key="data.access.mode" />.</td> 
		</tr>
	</table>
	<br />

	<h3>
		<fmt:message key="web.service" />
	</h3>

	<table class="form2">
		<tr>
			<td colspan="2"><b><fmt:message key="parabank.service" /></b>[&nbsp;
				<a href="<c:url value="services/ParaBank?wsdl"/>">WSDL</a>&nbsp;|&nbsp;<a
				href="<c:url value="services/bank?_wadl&_type=xml"/>">WADL</a>&nbsp;|&nbsp;<a
				href="<c:url value="/api-docs/index.html"/>">SWAGGER</a>&nbsp;]</td>
				<!-- Not ready yet |&nbsp;<a href="<c:url value="/raml/raml.api"/>">RAML</a>&nbsp; -->
		</tr>
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="web.service.soap_endpoint" />:</b></td>
			<td width="70%"><form:input cssClass="inputLarge"
					path="soapEndpoint" /></td>
		</tr>
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="web.service.rest_endpoint" />:</b></td>
			<td width="70%"><form:input cssClass="inputLarge"
					path="restEndpoint" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2"><fmt:message key="default.description" /></td>
		</tr>

		<tr>
			<td colspan="2"><hr /></td>
		</tr>
		<tr>
			<td colspan="2"><b><fmt:message key="loan.processor.service" /></b>
				[ <a href="<c:url value="services/LoanProcessor?wsdl"/>">WSDL</a> ]</td>
		</tr>
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="web.service.endpoint" />:</b></td>
			<td width="70%"><form:input cssClass="inputLarge"
					path="endpoint" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2"><fmt:message key="default.description" /></td>
		</tr>
	</table>
	<br />

	<h3>
		<fmt:message key="application.settings" />
	</h3>
	<table class="form2" >
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="initial.balance" />:</b> $</td>
			<td width="20%"><form:input cssClass="input"
					path="initialBalance" /></td>
			<td width="50%"><form:errors path="initialBalance"
					cssClass="error" /></td>
		</tr>
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="minimum.balance" />:</b> $</td>
			<td width="20%"><form:input cssClass="input"
					path="minimumBalance" /></td>
			<td width="50%"><form:errors path="minimumBalance"
					cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="3"><hr></td>
		</tr>
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="loan.provider" />:</b></td>
			<td width="20%"><form:select cssClass="input"
					path="loanProvider">
					<c:forEach var="provider" items="${loanProviders}">
						<fmt:message key="${provider}.label" var="label" />
						<form:option value="${provider}" label="${label}" />
					</c:forEach>
				</form:select></td>
			<td width="50%"><form:errors path="loanProvider"
					cssClass="error" /></td>
		</tr>
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="loan.processor" />:</b></td>
			<td width="20%"><form:select cssClass="input"
					path="loanProcessor">
					<c:forEach var="processor" items="${loanProcessors}">
						<fmt:message key="${processor}.label" var="label" />
						<form:option value="${processor}" label="${label}" />
					</c:forEach>
				</form:select></td>
			<td width="50%"><form:errors path="loanProcessor"
					cssClass="error" /></td>
		</tr>
		<tr>
			<td align="right" width="30%"><b><fmt:message
						key="loan.processor.threshold" />:</b></td>
			<td width="20%"><form:input cssClass="inputSmall"
					path="loanProcessorThreshold" />%</td>
			<td width="50%"><form:errors path="loanProcessorThreshold"
					cssClass="error" /></td>
		</tr>
	</table>

	<br />

	<input type="submit" class="button" value="<fmt:message key="submit"/>">

</form:form>