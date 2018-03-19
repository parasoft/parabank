<%@ include file="../include/include.jsp" %>

<h1 class="title"><fmt:message key="accounts.overview"/></h1>

<table id="accountTable" class="gradient-style" ng-app="OverviewAccountsApp" ng-controller="OverviewAccountsCtrl">
  <thead>
    <tr>
      <th><fmt:message key="account"/></th>
      <th><fmt:message key="balance"/></th>
      <th><fmt:message key="available.amount"/></th>
    </tr>
  </thead>
  <tbody>

    <tr ng-repeat="account in accounts">
        <td><a href="/parabank/activity.htm?id={{account.id}}">{{account.id}}</a></td>
        <td>{{account.balance | currency: "$" : 2}}</td>
        <td>{{account.availableBalance | currency: "$" : 2}}</td>
    </tr>

    <tr>
      <td align="right"><b><fmt:message key="total"/></b></td>
      <td><b>{{totalBalance | currency: "$" : 2}}</b></td>
      <td>&nbsp;</td>
    </tr>
  </tbody>
  <tfoot>
    <tr>
      <td colspan="3"><fmt:message key="balance.note"/></td>
    </tr>
  </tfoot>
</table>


<script>
    var app = angular.module('OverviewAccountsApp', []);
    app.controller('OverviewAccountsCtrl', function ($scope, $http) {
        $http.get("/parabank/services/bank/customers/" + ${model.customerId} + "/accounts")
            .then(function (response) {
                $scope.accounts = [];
                $scope.accounts = response.data;
                $scope.totalBalance = computeTotalBalance($scope.accounts);

                angular.forEach($scope.accounts, function(account) {
                    account.availableBalance = getAvailableBalance(account);
                });
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

    });
</script>