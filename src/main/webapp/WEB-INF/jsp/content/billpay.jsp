<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="BillPayApp" ng-controller="BillPayCtrl">

<div ng-show="showForm">
    <h1 class="title"><fmt:message key="bill.payment.service"/></h1>
    <p><fmt:message key="enter.payee.information"/></p>

    <form ng-submit="submit()">
        <table class="form2">
            <tr>
                <td align="right" width="30%"><b><fmt:message key="payee.name" />:</b></td>
                <td width="20%"><input ng-model="payee.name" class="input" name="payee.name" /></td>
                <td width="50%">
                    <span ng-show="!validationModel.name" class="error"><fmt:message key="error.payee.name.required" /></span>
                </td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="address" />:</b></td>
                <td width="20%"><input class="input" ng-model="payee.address.street" name="payee.address.street" /></td>
                <td width="50%"><span ng-show="!validationModel.address" class="error"><fmt:message key="error.address.required" /></span></td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="city" />:</b></td>
                <td width="20%"><input class="input" ng-model="payee.address.city" name="payee.address.city" /></td>
                <td width="50%"><span ng-show="!validationModel.city" class="error"><fmt:message key="error.city.required" /></span></td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="state" />:</b></td>
                <td width="20%"><input class="input" ng-model="payee.address.state" name="payee.address.state" /></td>
                <td width="50%"><span ng-show="!validationModel.state" class="error"><fmt:message key="error.state.required" /></span></td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="zip.code" />:</b></td>
                <td width="20%"><input class="input" ng-model="payee.address.zipCode" name="payee.address.zipCode" /></td>
                <td width="50%"><span ng-show="!validationModel.zipCode" class="error"><fmt:message key="error.zip.code.required" /></span></td>
            </tr>
            <tr>
                <%-- Random id and class attributes in "payee.phoneNamber" are for demonstrating
             better locator recommendations in Selenium test scenarios. --%>
                <%
                    String phoneNumberRandomId = java.util.UUID.randomUUID().toString();
                        String phoneNumberclass = String.format("input phone-number-%s", phoneNumberRandomId);
                %>
                <td align="right" width="30%"><b><fmt:message key="phone.number" />:</b></td>
                <td width="20%"><input id="<%=phoneNumberRandomId%>" class="<%=phoneNumberclass%>" ng-model="payee.phoneNumber" name="payee.phoneNumber" /></td>
                <td width="50%"><span ng-show="!validationModel.phoneNumber" class="error"><fmt:message key="error.phone.number.required" /></span></td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="payee.account" />:</b></td>
                <td><input class="input" ng-model="payee.accountNumber" name="payee.accountNumber" /></td>
                <td width="50%">
                    <span ng-show="validationModel.account == 'empty'" class="error"><fmt:message key="error.account.number.required" /></span>
                    <span ng-show="validationModel.account == 'invalid'" class="error"><fmt:message key="typeMismatch.java.lang.Integer" /></span>
                </td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="verify.account" />:</b></td>
                <td width="20%"><input class="input" ng-model="verifyAccount" name="verifyAccount" /></td>
                <td width="50%">
                    <span ng-show="validationModel.verifyAccount == 'empty'" class="error"><fmt:message key="error.account.number.required" /></span>
                    <span ng-show="validationModel.verifyAccount == 'invalid'" class="error"><fmt:message key="typeMismatch.java.lang.Integer" /></span>
                    <span ng-show="validationModel.verifyAccount == 'mismatch'" class="error"><fmt:message key="error.account.number.mismatch" /></span>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="right" width="30%"><b><fmt:message key="billpay.amount" />: $</b></td>
                <td width="20%"><input class="input" ng-model="amount" name="amount" /></td>
                <td width="50%">
                    <span ng-show="validationModel.amount == 'empty'" class="error"><fmt:message key="error.amount.empty" /></span>
                    <span ng-show="validationModel.amount == 'invalid'" class="error"><fmt:message key="typeMismatch.java.math.BigDecimal" /></span>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="right"><b><fmt:message key="from.account.number" />:</b></td>
                <td>
                    <select name="fromAccountId" class="input" ng-init="accountId='${accounts[0]}'"  ng-model="accountId">
                        <c:forEach items="${accounts}" var="account">
                            <option value="${account}">${account}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" class="button" value="<fmt:message key="send.payment"/>"></td>
            </tr>
        </table>
    </form>
</div>

<div ng-show="showResult">
    <h1 class="title"><fmt:message key="bill.payment.complete"/></h1>
    <p>
        <fmt:message key="billpay.confirmation">
            <fmt:param value="<span id='payeeName'>{{result.payeeName}}</span>"/>
            <fmt:param value="<span id='amount'>{{result.amount}}</span>"/>
            <fmt:param value="<span id='fromAccountId'>{{result.fromAccountId}}</span>"/>
        </fmt:message>
    </p>
    <p><fmt:message key="see.account.activity"/></p>
</div>

<div ng-show="showError">
     <h1 class="title"><fmt:message key="error.heading" /></h1>
     <p class="error"><fmt:message key="error.internal" /></p>
</div>

</div>

<script type="text/javascript">

	var app = angular.module('BillPayApp', []);
	app.controller('BillPayCtrl', function($scope, $http) {
        $scope.showForm = true;
        $scope.showResult = false;
        $scope.payee = {
        		address : { }
        };
        $scope.result = {};
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
        
        var showError= function(error) {
            $scope.validationModel = createValidationModel();
            $scope.showForm = false;
            $scope.showResult = false;
            $scope.showError = true;
            var status = error.status > 0 ? error.status : "timeout";
            var data = error.data ? error.data : "Server timeout"
            console.error("Server returned " + status + ": " + data);
        }
        
        var validate = function() {
            $scope.validationModel.name = isNonEmpty($scope.payee.name);
        	var address = $scope.payee.address;
            $scope.validationModel.address = isNonEmpty(address.street);
        	$scope.validationModel.city = isNonEmpty(address.city);
        	$scope.validationModel.state = isNonEmpty(address.state);
        	$scope.validationModel.zipCode = isNonEmpty(address.zipCode);
        	$scope.validationModel.phoneNumber = isNonEmpty($scope.payee.phoneNumber);
        	var account = $scope.payee.accountNumber;
        	$scope.validationModel.account = validateNumber($scope.payee.accountNumber);
        	var verifyAccount = $scope.verifyAccount;
        	$scope.validationModel.verifyAccount = validateNumber(verifyAccount);
        	if ($scope.validationModel.verifyAccount == null && verifyAccount !== account) {
        		$scope.validationModel.verifyAccount = 'mismatch';
        	}
        	$scope.validationModel.amount = validateNumber($scope.amount)
        	//look thru validation model for a false || string value which
        	//indicates an error
        	var valid = true;
        	for (prop in $scope.validationModel) {
        		var value = $scope.validationModel[prop];
        		if (value === false || typeof value === 'string') {
        			valid = false;
        			break
        		}
        	}
        	return valid;
        }
        
        $scope.validationModel = createValidationModel();
        $scope.submit = function() {
            if (!validate()) {
            	return;
            } 
            $http.post(
            	    'services_proxy/bank/billpay?accountId='+ this.accountId + '&amount=' + this.amount,
            		this.payee, 
            		{timeout: 30000}
            ).then(function(response) {
                $scope.result.payeeName = response.data.payeeName;
                $scope.result.amount = currencyFormat(response.data.amount);
                $scope.result.fromAccountId = response.data.accountId;
                $scope.showForm = false;
                $scope.showResult = true;
                document.title = 'ParaBank | ' + '<fmt:message key="billpayConfirm.title" />'
                })
            .catch(function(response) {
            	showError(response);
            });
        }
       
       
	});
</script>