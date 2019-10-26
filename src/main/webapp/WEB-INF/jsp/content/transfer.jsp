<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="TransferApp" ng-controller="TransferCtrl" ng-cloak>

   <div ng-if="showForm">

       <h1 class="title"><fmt:message key="transfer.funds"/></h1>
       <p id="amount.errors" ng-if="showEmptyAmountError" class="error"><fmt:message key="error.amount.empty"/></p>
       <p id="amount.errors" ng-if="showInvalidValueError" class="error"><fmt:message key="typeMismatch.java.math.BigDecimal"/></p>
       <form ng-submit="submit()">

         <p><b><fmt:message key="transfer.amount"/>:</b> $<input id="amount" type="text" name="input" ng-model="accounts.amount" /></p>
         <div>
            <fmt:message key="from.account.number"/>
            <select id="fromAccountId" class="input" ng-init="getAccounts()" ng-model="accounts.fromAccountId" ng-options="account.id for account in accounts track by account.id"><select>
            <fmt:message key="to.account.number"/>
            <select id="toAccountId" class="input" ng-model="accounts.toAccountId" ng-options="account.id for account in accounts track by account.id"><select>
         </div>
         <br/>
         <div><input type="submit" class="button" value="<fmt:message key="transfer"/>"></div>
 
       </form>
   </div>

   <div ng-if="showResult">
      <h1 class="title"><fmt:message key="transfer.complete"/></h1>

      <p>
	  <fmt:message key="transfer.confirmation">
	     <fmt:param value="<span id='amount'>{{amount | currency: '$' : 2 | commaLess}}</span>"/>
	     <fmt:param value="<span id='fromAccountId'>{{fromAccountId}}</span>"/>
	     <fmt:param value="<span id='toAccountId'>{{toAccountId}}</span>"/>
	  </fmt:message>
	  </p>
      <p><fmt:message key="see.account.activity"/></p>
   </div>
   
   <div ng-if="showError">
     <h1 class="title"><fmt:message key="error.heading" /></h1>
     <p class="error"><fmt:message key="error.internal" /></p>
   </div>
 
</div>

<script>
    var app = angular.module('TransferApp', []);
    app.controller('TransferCtrl', function($scope, $http) {

        $scope.showForm = true;
        $scope.showResult = false;
        $scope.errorCode = -1;

        $scope.getAccounts = function() {
        	
            $http.get("services_proxy/bank/customers/${customerId}/accounts", {timeout:30000})
                .then(function(response) {
                    $scope.accounts = response.data;
                    $scope.accounts.fromAccountId = $scope.accounts[0];
                    $scope.accounts.toAccountId = $scope.accounts[0];
                })
                .catch(function(response) {
                    showError(response);
                });
        }

        $scope.submit = function() {
        	resetErrors();
            $scope.amount = $scope.accounts.amount;
            $scope.fromAccountId = $scope.accounts.fromAccountId.id;
            $scope.toAccountId =  $scope.accounts.toAccountId.id;

            var url = "services_proxy/bank/transfer?fromAccountId=" + $scope.fromAccountId + "&toAccountId=" + $scope.toAccountId +"&amount=" + $scope.amount;

        	$http.defaults.transformResponse = [];
            $http.post(url, "", {timeout:30000})
                .then(function(response) {
                    $scope.showForm = false;
                    $scope.showResult = true;
                    $scope.errorCode = response.status;
                }).catch(function(response) {
                    $scope.errorCode = response.status;
                    if ($scope.errorCode >= 400 && !$scope.amount) {
                        $scope.showEmptyAmountError = true;
                        return;
                    }
                    if ($scope.errorCode >= 400 && isNaN($scope.amount)) {
                        $scope.showInvalidValueError = true;
                        return;
                    }
                    showError(response);
                });
            
        }
        
        function resetErrors() {
            $scope.showEmptyAmountError = false;
            $scope.showInvalidValueError = false;
        }

        function showError(error) {
            resetErrors();
            $scope.showForm = false;
            $scope.showResult = false;
            $scope.showError = true;
            var status = error.status > 0 ? error.status : "timeout";
            var data = error.data ? error.data : "Server timeout"
            console.error("Server returned " + status + ": " + data);
        }

    });

	app.filter('commaLess', function() {
		return function(input) {
			return (input) ? input.toString().trim().replace(",","") : null;
		};
	});
</script>