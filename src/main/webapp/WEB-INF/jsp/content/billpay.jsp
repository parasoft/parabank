<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div>

<div id="billpayForm">
    <h1 class="title"><fmt:message key="bill.payment.service"/></h1>
    <p><fmt:message key="enter.payee.information"/></p>

    <form>
        <table class="form2">
            <tr>
                <td align="right" width="30%"><b><fmt:message key="payee.name" />:</b></td>
                <td width="20%"><input class="input" name="payee.name" /></td>
                <td width="50%">
                    <span style="display:none" id="validationModel-name" class="error"><fmt:message key="error.payee.name.required" /></span>
                </td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="address" />:</b></td>
                <td width="20%"><input class="input"  name="payee.address.street" /></td>
                <td width="50%"><span style="display:none" id="validationModel-address" class="error"><fmt:message key="error.address.required" /></span></td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="city" />:</b></td>
                <td width="20%"><input class="input" name="payee.address.city" /></td>
                <td width="50%"><span style="display:none" id="validationModel-city" class="error"><fmt:message key="error.city.required" /></span></td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="state" />:</b></td>
                <td width="20%"><input class="input" name="payee.address.state" /></td>
                <td width="50%"><span style="display:none" id="validationModel-state" class="error"><fmt:message key="error.state.required" /></span></td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="zip.code" />:</b></td>
                <td width="20%"><input class="input" name="payee.address.zipCode" /></td>
                <td width="50%"><span style="display:none" id="validationModel-zipCode" class="error"><fmt:message key="error.zip.code.required" /></span></td>
            </tr>
            <tr>
                <%-- Random id and class attributes in "payee.phoneNamber" are for demonstrating
             better locator recommendations in Selenium test scenarios. --%>
                <%
                    String phoneNumberRandomId = java.util.UUID.randomUUID().toString();
                        String phoneNumberclass = String.format("input phone-number-%s", phoneNumberRandomId);
                %>
                <td align="right" width="30%"><b><fmt:message key="phone.number" />:</b></td>
                <td width="20%"><input id="<%=phoneNumberRandomId%>" class="<%=phoneNumberclass%>" name="payee.phoneNumber" /></td>
                <td width="50%"><span style="display:none" id="validationModel-phoneNumber" class="error"><fmt:message key="error.phone.number.required" /></span></td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="payee.account" />:</b></td>
                <td><input class="input" name="payee.accountNumber" /></td>
                <td width="50%">
                    <span style="display:none" id="validationModel-account-empty" class="error"><fmt:message key="error.account.number.required" /></span>
                    <span style="display:none" id="validationModel-account-invalid" class="error"><fmt:message key="typeMismatch.java.lang.Integer" /></span>
                </td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="verify.account" />:</b></td>
                <td width="20%"><input class="input" name="verifyAccount" /></td>
                <td width="50%">
                    <span style="display:none" id="validationModel-verifyAccount-empty" class="error"><fmt:message key="error.account.number.required" /></span>
                    <span style="display:none" id="validationModel-verifyAccount-invalid" class="error"><fmt:message key="typeMismatch.java.lang.Integer" /></span>
                    <span style="display:none" id="validationModel-verifyAccount-mismatch" class="error"><fmt:message key="error.account.number.mismatch" /></span>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="billpay.amount" />: $</b></td>
                <td width="20%"><input class="input" name="amount" /></td>
                <td width="50%">
                    <span style="display:none" id="validationModel-amount-empty" class="error"><fmt:message key="error.amount.empty" /></span>
                    <span style="display:none" id="validationModel-amount-invalid" class="error"><fmt:message key="typeMismatch.java.math.BigDecimal" /></span>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="right"><b><fmt:message key="from.account.number" />:</b></td>
                <td>
                    <select name="fromAccountId" class="input">
                        <c:forEach items="${accounts}" var="account">
                            <option value="${account}">${account}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="button" class="button" value="<fmt:message key="send.payment"/>"></td>
            </tr>
        </table>
    </form>
</div>

<div id="billpayResult" style="display:none">
    <h1 class="title"><fmt:message key="bill.payment.complete"/></h1>
    <p>
        <fmt:message key="billpay.confirmation">
            <fmt:param value="<span id='payeeName'></span>"/>
            <fmt:param value="<span id='amount'></span>"/>
            <fmt:param value="<span id='fromAccountId'></span>"/>
        </fmt:message>
    </p>
    <p><fmt:message key="see.account.activity"/></p>
</div>

<div id="billpayError" style="display:none">
     <h1 class="title"><fmt:message key="error.heading" /></h1>
     <p class="error"><fmt:message key="error.internal" /></p>
</div>

</div>

<script type="text/javascript">
	$(document).ready(function() {
		var accountId= null;
		var verifyAccount = null;
		var amount = null;
		var showForm = function(visible) {
			if (visible) {
				$("#billpayForm").show();
			} else {
				$("#billpayForm").hide();
			}
		}
        var showResult = function(visible) {
			if (visible) {
				$("#billpayResult").show();
			} else {
				$("#billpayResult").hide();
			}	
        }
        var showBillpayError = function(visible) {
			if (visible) {
				$("#billpayError").show();
			} else {
				$("#billpayError").hide();
			}
        }
        var payee = {
			address : { }
        };
        
        var currencyFormat = function(amount) {
            return '$' + amount.toFixed(2);
        }
        
        var isNonEmpty = function(str) {
        	return str !== null && str !== undefined && str.trim() !== '';
        }
        
        var isNumber = function(num) {
            return !isNaN(parseFloat(num));
        }
        
        var validateNumber = function(num) {
        	if (!isNonEmpty(num)) {
        		return 'empty';
        	} else if (isNaN(parseFloat(num))) {
        		return 'invalid'
        	}
        	return null;
        }
        var createValidationModel = function() {
        	return { 
                name : true,
                address : true,
                city : true,
                state : true,
                zipCode : true,
                phoneNumber : true,
                account : null,
                verifyAccount : null,
                amount : null
            };
        }
        
        var updateModels = function() {
        	//payee
	        payee.name = $("[name='payee.name']").val();
	        payee.address.street = $("[name='payee.address.street']").val();
	        payee.address.city = $("[name='payee.address.city']").val();
	        payee.address.state = $("[name='payee.address.state']").val();
	        payee.address.zipCode = $("[name='payee.address.zipCode']").val();
	        payee.phoneNumber = $("[name='payee.phoneNumber']").val();
	        payee.accountNumber = $("[name='payee.accountNumber']").val();
	        
	        //account
	        accountId = $("[name='fromAccountId']").val();
	        verifyAccount = $("[name='verifyAccount']").val();
	        amount = $("[name='amount']").val();
        }
        
        var showError= function(error) {
            validationModel = createValidationModel();
            showForm(false);
            showResult(false);
            showBillpayError(true);
            var status = error.status > 0 ? error.status : "timeout";
            var data = error.data ? error.data : "Server timeout"
            console.error("Server returned " + status + ": " + data);
        }
        
        var validate = function() {
            isNonEmpty(payee.name) ? $("#validationModel-name").hide() : $("#validationModel-name").show();
        	var address = payee.address;
            isNonEmpty(address.street) ? $("#validationModel-address").hide() : $("#validationModel-address").show();
        	isNonEmpty(address.city) ? $("#validationModel-city").hide() : $("#validationModel-city").show();
        	isNonEmpty(address.state) ? $("#validationModel-state").hide() : $("#validationModel-state").show();
        	isNonEmpty(address.zipCode) ? $("#validationModel-zipCode").hide() : $("#validationModel-zipCode").show();
        	isNonEmpty(payee.phoneNumber) ? $("#validationModel-phoneNumber").hide() : $("#validationModel-phoneNumber").show();
        	var account = payee.accountNumber;
        	validationModel.account = validateNumber(payee.accountNumber);
        	validationModel.account == "empty" ? $("#validationModel-account-empty").show() : $("#validationModel-account-empty").hide();
        	validationModel.account == "invalid" ? $("#validationModel-account-invalid").show() : $("#validationModel-account-invalid").hide();
        	validationModel.verifyAccount = validateNumber(verifyAccount);
        	if (validationModel.verifyAccount == null && verifyAccount !== account) {
        		validationModel.verifyAccount = 'mismatch';
        	}
        	validationModel.verifyAccount == "empty" ? $("#validationModel-verifyAccount-empty").show() : $("#validationModel-verifyAccount-empty").hide();
        	validationModel.verifyAccount == "mismatch" ? $("#validationModel-verifyAccount-mismatch").show() : $("#validationModel-verifyAccount-mismatch").hide();
        	validationModel.verifyAccount == "invalid" ? $("#validationModel-verifyAccount-invalid").show() : $("#validationModel-verifyAccount-invalid").hide();
        	
        	validationModel.amount = validateNumber(amount)
        	validationModel.amount == "empty" ? $("#validationModel-amount-empty").show() : $("#validationModel-amount-empty").hide();
        	validationModel.amount == "invalid" ? $("#validationModel-amount-invalid").show() : $("#validationModel-amount-invalid").hide();
			
        	//look thru validation markers to see if any are visible
        	return $("[id^=validationModel]:visible").length == 0;
        }
        
        var validationModel = createValidationModel();
        
        var submit = function() {
            if (!validate()) {
            	return;
            } 
            $.ajax({
            	  url: 'services_proxy/bank/billpay?accountId='+ accountId + '&amount=' + amount,
            	  type: 'POST',
            	  contentType: 'application/json',
            	  data: JSON.stringify(payee),
            	  success: function(response) {
					$("#payeeName").text(response.payeeName);
					$("#amount").text(currencyFormat(response.amount));
              		$("#fromAccountId").text(response.accountId);
              		showForm(false);
					showResult(true);
              		document.title = 'ParaBank | ' + '<fmt:message key="billpayConfirm.title" />'
            	  },
            	  error: function(xhr, status, error) {
            		  showError(error);
            	  }
            	});
        }
        $("input[type=button]").click(() => {
        	updateModels();
        	submit(); 
		});
	});
</script>