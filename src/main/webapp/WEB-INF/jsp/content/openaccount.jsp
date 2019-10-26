<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="AddAccountApp" ng-controller="AddAccountCtrl" ng-cloak>
  <div ng-if="showForm">
    <h1 class="title"><fmt:message key="open.new.account" /></h1>
    <form ng-submit="submit()">
      <p><b><fmt:message key="what.type.of.account" /></b></p>
      <select id="type" class="input" ng-init="types.selectedOption = '0'" ng-model="types.selectedOption" >
        <c:forEach items="${types}" var="type" varStatus="loop">
          <option value="${loop.index}">${type}</option>
        </c:forEach>
      </select>
      <br /><br />
      <fmt:formatNumber type="currency" value="${minimumBalance}" var="minValue" currencySymbol="$" maxFractionDigits="2" minFractionDigits="2" />
      <p><b><fmt:message key="minimum.deposit"><fmt:param value="${minValue}" /></fmt:message></b></p>
      <select id="fromAccountId" class="input" ng-init="getAccounts()" ng-model="accounts.selectedOption" ng-options="account.id for account in accounts track by account.id"></select>
      <br /><br />
      <div><input type="submit" class="button" value="<fmt:message key="open.new.account"/>"></div>
    </form>
  </div>

  <div ng-if="showResult">
    <h1 class="title"><fmt:message key="account.opened" /></h1>
    <p><fmt:message key="congratulations" /></p>
    <p><b><fmt:message key="new.account.number" />:</b> <a id="newAccountId" href="{{newAccountUrl}}">{{newAccountId}}</a></p>
  </div>

  <div ng-if="showError">
    <h1 class="title"><fmt:message key="error.heading" /></h1>
    <p class="error"><fmt:message key="error.internal" /></p>
  </div>
</div>

<script>
    var app = angular.module('AddAccountApp', []);
    app.controller('AddAccountCtrl', function($scope, $http) {

        $scope.showForm = true;
        $scope.showResult = false;
        $scope.showError = false;
        $scope.types = [];
        $scope.types.selectedOption;

        $scope.getAccounts = function() {
            $http.get("services_proxy/bank/customers/${customerId}/accounts", {timeout:30000})
                .then(function(response) {
                    $scope.accounts = response.data;
                    $scope.accounts.selectedOption = $scope.accounts[0];
                })
                .catch(function(response) {
                    showError(response);
                });
        }

        $scope.submit = function() {
            var url = "services_proxy/bank/createAccount?customerId=${customerId}&newAccountType="+ $scope.types.selectedOption + "&fromAccountId=" + $scope.accounts.selectedOption.id;
            $http.post(url, "", {timeout:30000})
                .then(function(response) {
                    $scope.showForm = false;
                    $scope.showResult = true;
                    $scope.newAccountId = response.data.id;
                    $scope.newAccountUrl = "activity.htm" + "?id=" + $scope.newAccountId;
                })
                .catch(function(response) {
                    showError(response);
                });
        }
        
        function showError(error) {
            $scope.showForm = false;
            $scope.showResult = false;
            $scope.showError = true;
            var status = error.status > 0 ? error.status : "timeout";
            var data = error.data ? error.data : "Server timeout"
            console.error("Server returned " + status + ": " + data);
        }
    });
</script>