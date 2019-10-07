<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="RequestLoanApp" ng-controller="RequestLoanAppCtrl" ng-cloak>

    <div ng-if="showForm">
	   <h1 class="title">
		  <fmt:message key="apply.for.a.loan" />
	   </h1>

	   <form ng-submit="submit()">
		<table class="form2">
			<tr>
				<td align="right" width="40%"><b><fmt:message key="loan.amount" />:</b> $</td>
					<td width="20%"><input disabled id="amount" class="input" ng-model="loanRequest.amount" /></td>
					<td width="40%"></td>
			</tr>
			<tr>
				<td align="right" width="40%"><b><fmt:message key="down.payment" />:</b> $</td>
				<td width="20%"><input disabled id="downPayment" class="input" ng-model="loanRequest.downPayment" /></td>
				<td width="40%"></td>
			</tr>
			<tr>
				<td align="right" width="40%"><b><fmt:message key="from.account.number" />:</b></td>
				<td width="20%">
				    <select style="visibility:hidden" id="fromAccountId" class="input" ng-init="loanRequest.fromAccountId = '${accounts[0]}'" ng-model="loanRequest.fromAccountId">
                        <c:forEach items="${accounts}" var="account">
                            <option value="${account}">${account}</option>
                        </c:forEach>
				    </select>
                </td>
                <td width="40%"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="2">
					<div>
						<input id="submit" type="submit" class="button" value="<fmt:message key="apply.now"/>">
					</div>
					<div style="position:relative; background-color:rgb(220, 220, 220)" id="curtain">
						<fmt:message key="loading"/>
					</div>
				</td>
			</tr>
		</table>
		<br>
	</form>
    </div>
    
    <div ng-if="showResult">
        <h1 class="title"><fmt:message key="loan.request.processed"/></h1>
        <table class="form" style="width: 500px;">
            <tr>
                <td align="right" width="25%"><b><fmt:message key="loan.provider.name"/>:</b></td>
                <td id="loanProviderName" width="75%">{{loanResponse.loanProviderName}}</td>
            </tr>
            <tr>
                <td align="right"><b><fmt:message key="loan.response.date"/>:</b></td>
                <td id="responseDate">{{loanResponse.formattedDate}}</td>
            </tr>
            <tr>
                <td align="right"><b><fmt:message key="loan.status"/>:</b></td>
                <td ng-if="loanResponse.approved" id="loanStatus"><fmt:message key="loan.approved"/></td>
                <td ng-if="!loanResponse.approved" id="loanStatus"><fmt:message key="loan.denied"/></td>
            </tr>
        </table>
        <br/>
        <div ng-if="!loanResponse.approved">
            <p ng-if="loanResponse.message === 'error.insufficient.funds.for.down.payment'" class="error"><fmt:message key="error.insufficient.funds.for.down.payment"/></p>
            <p ng-if="loanResponse.message === 'error.insufficient.funds'" class="error"><fmt:message key="error.insufficient.funds"/></p>
            <p ng-if="loanResponse.message === 'error.insufficient.funds.and.down.payment'" class="error"><fmt:message key="error.insufficient.funds.and.down.payment"/></p>
            <p ng-if="loanResponse.message === 'error.insufficient.down.payment'" class="error"><fmt:message key="error.insufficient.down.payment"/></p>
        </div>
        <div ng-if="loanResponse.approved">
            <p><fmt:message key="loan.approved.message"/></p>    
            <p><b><fmt:message key="new.account.number"/>:</b> <a id="newAccountId" href="${pageContext.request.contextPath}/activity.htm?id={{loanResponse.accountId}}">{{loanResponse.accountId}}</a></p>
        </div>
    </div>

	<div ng-if="showError">
		<h1 class="title">
			<fmt:message key="error.heading" />
		</h1>
		<p class="error">
			<fmt:message key="error.internal" />
		</p>
	</div>

</div>

<script>

setTimeout(setCurtain);
setTimeout(function() {
	document.getElementById("amount").disabled = false;
	document.getElementById("downPayment").disabled = false;
}, 3000);
setTimeout(function() { document.getElementById("fromAccountId").style.visibility = "visible"; }, 6000);
setTimeout(function() { document.getElementById("curtain").style.visibility = "hidden"; }, 9000);

function setCurtain() {
    var button = document.getElementById("submit");
    var width = button.getBoundingClientRect().width;
    var height = button.getBoundingClientRect().height;
    if (width > 0 && height > 0) {
        var curtain = document.getElementById("curtain");
        curtain.style.visibility = "visible";
        curtain.style.position = "relative";
        curtain.style.width = width + "px";
        curtain.style.height = height + "px";
        curtain.style.top = -height + "px";
    } else {
        setTimeout(setCurtain, 100);
    }
}

var app = angular.module('RequestLoanApp', []);

app.controller('RequestLoanAppCtrl', function ($scope, $rootScope, $http) {
	
	$scope.loanRequest = { };
	$scope.showForm = true;
    $scope.showResult = false;
    $scope.showError = false;
    
	$scope.submit = function() {
		var url = 'services_proxy/bank/requestLoan?customerId=${customerId}&amount=' + $scope.loanRequest.amount + '&downPayment=' + $scope.loanRequest.downPayment + '&fromAccountId=' + $scope.loanRequest.fromAccountId;
		$http.post(url, "", {timeout:30000})
		  .then(function(response) {
			  $scope.showForm = false;
              $scope.showResult = true;
			  $scope.loanResponse = response.data;
			  $scope.loanResponse.formattedDate = format(
					  new Date(response.data.responseDate));
		  })
		  .catch(function(response) {
			  showError(response);
		  })
	}
	
    function showError(error) {
        $scope.showForm = false;
        $scope.showResult = false;
        $scope.showError = true;
        var status = error.status > 0 ? error.status : "timeout";
        var data = error.data ? error.data : "Server timeout"
            console.error("Server returned " + status + ": " + data);
    }
    
    function format(date) {
        var month = date.getMonth() + 1 + "";
        if (month.length === 1) {
        	   month = "0" + month;
        }
        return month + '-' + date.getDate() + '-' +  date.getFullYear();
    }
});

</script>