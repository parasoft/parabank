<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="AccountDetailsApp" ng-controller="rootCtrl" ng-cloak>
  <div ng-if="showDetails" ng-controller="AccountDetailsCtrl">
    <h1 class="title"><fmt:message key="account.details" /></h1>

    <table>
      <tr>
        <td align="right"><fmt:message key="account.number" />:</td>
        <td id="accountId">{{account.id}}</td>
      </tr>
      <tr>
        <td align="right"><fmt:message key="account.type" />:</td>
        <td id="accountType">{{account.type}}</td>
      </tr>
      <tr>
        <td align="right"><fmt:message key="account.balance" />:</td>
        <td id="balance">{{account.balance | currency: "$" : 2 | commaLess}}</td>
      </tr>
      <tr>
        <td align="right"><fmt:message key="account.available.balance" />:</td>
        <td id="availableBalance">{{account.availableBalance | currency: "$" : 2 | commaLess}}</td>
      </tr>
    </table>

    <br />
  </div>
  <div ng-if="showActivity" ng-controller="AccountActivityCtrl">
    <h1 class="title"><fmt:message key="account.activity" /></h1>
    <form ng-submit="submit()">
      <table class="form_activity">
        <tr>
          <td align="right"><b><fmt:message key="activity.period" />:</b></td>
          <td>
            <select id="month" name="month" class="input" ng-init="activityPeriod = '${months[0]}'" ng-model="activityPeriod">
              <c:forEach items="${months}" var="month">
                <option value="${month}">${month}</option>
              </c:forEach>
            </select>
          </td>
        </tr>
        <tr>
          <td align="right"><b><fmt:message key="transaction.type" />:</b></td>
          <td>
            <select id="transactionType" name="transactionType" class="input" ng-init="type = '${types[0]}'" ng-model="type">
              <c:forEach items="${types}" var="type">
                <option value="${type}">${type}</option>
              </c:forEach>
            </select>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td><input type="submit" class="button" value="Go"></td>
        </tr>
      </table>
    </form>

    <br />

    <p ng-if="transactions.length <= 0">
      <b><fmt:message key="no.transactions.found" /></b>
    </p>

    <table ng-if="transactions.length > 0" id="transactionTable" class="gradient-style">
      <thead>
        <tr>
          <th><fmt:message key="date" /></th>
          <th><fmt:message key="transaction" /></th>
          <th><fmt:message key="debit" /></th>
          <th><fmt:message key="credit" /></th>
        </tr>
      </thead>
      <tbody>
        <tr ng-repeat="transaction in transactions">
          <td>{{transaction.date | date:'MM-dd-yyyy'}}</td>
          <td><a href="transaction.htm?id={{transaction.id}}">{{transaction.description}}</a></td>

          <td ng-if="transaction.type == 'Debit'">{{transaction.amount | currency: "$" : 2 | commaLess}}</td>
          <td ng-if="transaction.type != 'Debit'"></td>

          <td ng-if="transaction.type == 'Credit'">{{transaction.amount | currency: "$" : 2 | commaLess}}</td>
          <td ng-if="transaction.type != 'Credit'"></td>
        </tr>
      </tbody>
    </table>
  </div>
  
  <div ng-if="showError">
    <h1 class="title"><fmt:message key="error.heading" /></h1>
    <p class="error"><fmt:message key="error.internal" /></p>
  </div>

</div>

<script>
    var app = angular.module('AccountDetailsApp', []);

    app.controller('AccountDetailsCtrl', function ($scope, $rootScope, $http) {
        $http.get("services_proxy/bank/accounts/${model.accountId}", {timeout:30000})
            .then(function (response) {
                $scope.account = response.data;
                $scope.account.availableBalance = getAvailableBalance($scope.account);
            })
            .catch(function(response) {
                reportError($rootScope, response);
            });

        function getAvailableBalance(account) {
            return account.balance < 0 ? 0 : account.balance;
        }
    });

    app.controller('AccountActivityCtrl', function ($scope, $rootScope, $http) {
        $http.get("services_proxy/bank/accounts/${model.accountId}/transactions", {timeout:30000})
            .then(function (response) {
                $scope.transactions = [];
                $scope.transactions = response.data;
            })
            .catch(function(response) {
                reportError($rootScope, response);
            });

        $scope.submit = function() {
            $http.get("services_proxy/bank/accounts/${model.accountId}/transactions/month/" + $scope.activityPeriod + "/type/" + $scope.type, {timeout:30000})
                .then(function (response) {
                    $scope.transactions = response.data;
                })
                .catch(function(response) {
                    reportError($rootScope, response);
                });
        };
    });
    
    app.controller('rootCtrl', function ($rootScope) {
        $rootScope.showDetails = true;
        $rootScope.showActivity = true;
    });
    
    function reportError(scope, error){
        scope.showDetails = false;
        scope.showActivity = false;
        scope.showError = true;
        
        var status = error.status > 0 ? error.status : "timeout";
        var data = error.data ? error.data : "Server timeout"
        console.error("Server returned " + status + ": " + data);
    }

	app.filter('commaLess', function() {
		return function(input) {
			return (input) ? input.toString().trim().replace(",","") : null;
		};
	});
</script>