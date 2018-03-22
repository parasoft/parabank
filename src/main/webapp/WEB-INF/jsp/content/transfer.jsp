<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="TransferApp" ng-controller="TransferCtrl">

   <div ng-if="showForm">

       <h1 class="title"><fmt:message key="transfer.funds"/></h1>
       <div ng-if="showError">
          <p ng-if="errorCode >= 400 && !amount" class="error"><fmt:message key="error.amount.empty"/></p>
          <p ng-if="errorCode >= 400 && amount && !isNumber(amount)" class="error"><fmt:message key="typeMismatch.java.math.BigDecimal"/></p>
       </div>

       <form ng-submit="submit()">

         <p><b><fmt:message key="transfer.amount"/>:</b> $<input type="text" name="input" ng-model="accounts.amount" /></p>
         <div>
            <fmt:message key="from.account.number"/>
            <select class="input" ng-init="getAccounts()" ng-model="accounts.fromAccountId" ng-options="account.id for account in accounts track by account.id"><select>
            <fmt:message key="to.account.number"/>
            <select class="input" ng-model="accounts.toAccountId" ng-options="account.id for account in accounts track by account.id"><select>
         </div>
         <br/>
         <div><input type="submit" class="button" value="<fmt:message key="transfer"/>"></div>
 
       </form>
   </div>

   <div ng-if="showResult">
      <h1 class="title"><fmt:message key="transfer.complete"/></h1>

      <p>
	  <fmt:message key="transfer.confirmation">
	     <fmt:param value="<span id='amount'>{{amount | currency: '$' : 2}}</span>"/>
	     <fmt:param value="<span id='fromAccountId'>{{fromAccountId}}</span>"/>
	     <fmt:param value="<span id='toAccountId'>{{toAccountId}}</span>"/>
	  </fmt:message>
	  </p>
      <p><fmt:message key="see.account.activity"/></p>
   </div> 
 
</div>

<script>
    var app = angular.module('TransferApp', []);
    app.controller('TransferCtrl', function($scope, $http) {

        $scope.showForm = true;
        $scope.showResult = false;
        $scope.showError = false;
        $scope.errorCode = -1;

        $scope.getAccounts = function() {
        	
            $http.get("/parabank/services/bank/customers/${customerId}/accounts")
                .then(function(response) {
                    $scope.accounts = response.data;
                    $scope.accounts.fromAccountId = $scope.accounts[0];
                    $scope.accounts.toAccountId = $scope.accounts[0];
                });
        }

        $scope.submit = function() {
        	
            $scope.amount = $scope.accounts.amount;
            $scope.fromAccountId = $scope.accounts.fromAccountId.id;
            $scope.toAccountId =  $scope.accounts.toAccountId.id;

            var url = "services/bank/transfer?fromAccountId=" + $scope.fromAccountId + "&toAccountId=" + $scope.toAccountId +"&amount=" + $scope.amount;

        	$http.defaults.transformResponse = [];
            $http.post(url)
                .then(function(response) {
                    $scope.showForm = false;
                    $scope.showError = false;
                    $scope.showResult = true;
                    $scope.errorCode = response.status;
                }).catch(function(response) {
                    $scope.showError = true;
                    $scope.errorCode = response.status;
                });
        }

    });
</script>