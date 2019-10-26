<%@ include file="../include/include.jsp" %>

<div ng-app="OverviewAccountsApp" ng-controller="OverviewAccountsCtrl" ng-cloak>
  <div ng-if="showOverview">
    <h1 class="title"><fmt:message key="accounts.overview" /></h1>
    <table id="accountTable" class="gradient-style">
      <thead>
        <tr>
          <th><fmt:message key="account" /></th>
          <th><fmt:message key="balance" /></th>
          <th><fmt:message key="available.amount" /></th>
        </tr>
      </thead>
      <tbody>

        <tr ng-repeat="account in accounts">
          <td><a href="activity.htm?id={{account.id}}">{{account.id}}</a></td>
          <td>{{account.balance | currency: "$" : 2 | commaLess}}</td>
          <td>{{account.availableBalance | currency: "$" : 2 | commaLess}}</td>
        </tr>

        <tr>
          <td align="right"><b><fmt:message key="total" /></b></td>
          <td><b>{{totalBalance | currency: "$" : 2 | commaLess}}</b></td>
          <td>&nbsp;</td>
        </tr>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="3"><fmt:message key="balance.note" />
        </tr>
      </tfoot>
    </table>

  </div>

  <div ng-if="showError">
    <h1 class="title"><fmt:message key="error.heading" /></h1>
    <p class="error"><fmt:message key="error.internal" /></p>
  </div>
</div>

<script>
    var app = angular.module('OverviewAccountsApp', []);
    app.controller('OverviewAccountsCtrl', function ($scope, $http) {
        $scope.showOverview = true;
        $scope.showError = false;
        
        $http.get("services_proxy/bank/customers/" + ${model.customerId} + "/accounts", {timeout:30000})
            .then(function (response) {
                $scope.accounts = [];
                $scope.accounts = response.data;
                $scope.totalBalance = computeTotalBalance($scope.accounts);

                angular.forEach($scope.accounts, function(account) {
                    account.availableBalance = getAvailableBalance(account);
                });
            })
            .catch(function (response){
                showError(response);
            });

        function getAvailableBalance(account) {
            return account.balance < 0 ? 0 : account.balance;
        }

        function computeTotalBalance(accounts) {
            var totalBalance = 0.0;
            angular.forEach(accounts, function(account) {
                totalBalance = totalBalance + parseFloat(account.balance, 10);
            });
            return totalBalance;
        }
        
        function showError(error) {
            $scope.showOverview = false;
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