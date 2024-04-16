<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div>

    <div id="requestLoanForm">
	   <h1 class="title">
		  <fmt:message key="apply.for.a.loan" />
	   </h1>

	   <form>
		<table class="form2">
			<tr>
				<td align="right" width="40%"><b><fmt:message key="loan.amount" />:</b> $</td>
					<td width="20%"><input id="amount" class="input"/></td>
					<td width="40%"></td>
			</tr>
			<tr>
				<td align="right" width="40%"><b><fmt:message key="down.payment" />:</b> $</td>
				<td width="20%"><input id="downPayment" class="input"/></td>
				<td width="40%"></td>
			</tr>
			<tr>
				<td align="right" width="40%"><b><fmt:message key="from.account.number" />:</b></td>
				<td width="20%">
				    <select id="fromAccountId" class="input" >
                        <c:forEach items="${accounts}" var="account">
                            <option value="${account}">${account}</option>
                        </c:forEach>
				    </select>
                </td>
                <td width="40%"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="2"><input type="button" class="button" value="<fmt:message key="apply.now"/>"></td>
			</tr>
		</table>
		<br>
	</form>
    </div>
    
    <div id="requestLoanResult" style="display:none">
        <h1 class="title"><fmt:message key="loan.request.processed"/></h1>
        <table class="form" style="width: 500px;">
            <tr>
                <td align="right" width="25%"><b><fmt:message key="loan.provider.name"/>:</b></td>
                <td id="loanProviderName" width="75%"></td>
            </tr>
            <tr>
                <td align="right"><b><fmt:message key="loan.response.date"/>:</b></td>
                <td id="responseDate"></td>
            </tr>
            <tr>
                <td align="right"><b><fmt:message key="loan.status"/>:</b></td>
                <td id="loanStatus"></td>
            </tr>
        </table>
        <br/>
        <div id="loanRequestDenied" style="display:none">
            <p class="error"></p>
        </div>
        <div id="loanRequestApproved" style="display:none">
            <p><fmt:message key="loan.approved.message"/></p>    
            <p><b><fmt:message key="new.account.number"/>:</b> <a id="newAccountId" href=""></a></p>
        </div>
    </div>

	<div id="requestLoanError" style="display:none">
		<h1 class="title">
			<fmt:message key="error.heading" />
		</h1>
		<p class="error">
			<fmt:message key="error.internal" />
		</p>
	</div>

</div>
<script>
	$(document).ready(function() {
		var showForm = function(visible) {
			if (visible) {
				$("#requestLoanForm").show();
			} else {
				$("#requestLoanForm").hide();
			}
		}
		var showResult = function(visible) {
			if (visible) {
				$("#requestLoanResult").show();
			} else {
				$("#requestLoanResult").hide();
			}
		}
		
		 function format(date) {
			var month = date.getMonth() + 1 + "";
			if (month.length === 1) {
				month = "0" + month;
			}
			return month + '-' + date.getDate() + '-' +  date.getFullYear();
		}
		
		var submit = function() {
			var url = 'services_proxy/bank/requestLoan?customerId=${customerId}&amount=' + $("#amount").val() + '&downPayment=' + $("#downPayment").val() + '&fromAccountId=' + $("#fromAccountId").val();
			$.ajax({
				url: url,
				type: 'POST',
				success: function(response) {
					showForm(false);
					showResult(true);
					$("#loanProviderName").html(response.loanProviderName);
					$("#responseDate").html(format(new Date(response.responseDate)));
					$("#loanStatus").html(response.approved ? '<fmt:message key="loan.approved"/>' : '<fmt:message key="loan.denied"/>');
					if (response.approved)  {
						$("#loanRequestApproved").show();
					} else {
						$("#loanRequestDenied").show();					
					}
					$("#newAccountId").attr("href", "${pageContext.request.contextPath}/activity.htm?id=" + response.accountId).text(response.accountId);
					if (response.message === 'error.insufficient.funds.for.down.payment') {
						$('#loanRequestDenied p.error').html('<fmt:message key="error.insufficient.funds.for.down.payment"/>');
					}
		            if (response.message === 'error.insufficient.funds.and.down.payment') {
			            $('#loanRequestDenied p.error').html('<fmt:message key="error.insufficient.funds.and.down.payment"/>');
		            }
		            if (response.message === 'error.insufficient.funds') {
			            $('#loanRequestDenied p.error').html('<fmt:message key="error.insufficient.funds"/>');
		            }
		            if (response.message === 'error.insufficient.down.payment') {
			            $('#loanRequestDenied p.error').html('<fmt:message key="error.insufficient.down.payment"/>');
		            }
				},
				error: function(response) {
					showForm(false);
					showResult(false);
					$("#requestLoanError").show();
				}
			})
		}
		
		$("input[type=button]").click(() => {
        	submit(); 
		});
		
	})
</script>