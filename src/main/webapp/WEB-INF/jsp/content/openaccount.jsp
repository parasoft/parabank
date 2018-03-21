<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="AddAccountApp" ng-controller="AddAccountCtrl">
    <div ng-if="show">
        <h1 class="title"><fmt:message key="open.new.account"/></h1>
        <form ng-submit="submit()">
            <p><b><fmt:message key="what.type.of.account"/></b></p>
            <select class="input" ng-init="getTypes()" ng-model="types.selectedOption" ng-options="type.name for type in types track by type.id"></select>
            <br/>
            <br/>
            <fmt:formatNumber type="currency" value="${minimumBalance}" var="minValue" currencySymbol="$" maxFractionDigits="2" minFractionDigits="2"/>
            <p><b><fmt:message key="minimum.deposit"><fmt:param value="${minValue}"/></fmt:message></b></p>
            <select class="input" ng-init="getAccounts()" ng-model="accounts.selectedOpton" ng-options="account.id for account in accounts track by account.id"><select>
            <br/>
            <br/>
            <div><input type="submit" class="button" value="<fmt:message key="open.new.account"/>"></div>
        </form>
    </div>
    <div ng-if="!show">
        <h1 class="title"><fmt:message key="account.opened"/></h1>
        <p><fmt:message key="congratulations"/></p>
        <p><b><fmt:message key="new.account.number"/>:</b> <a id="newAccountId" href="{{newAccountUrl}}">{{newAccountId}}</a></p>
    </div>
</div>

<script>
    var app = angular.module('AddAccountApp', []);
    app.controller('AddAccountCtrl', function($scope, $http) {

        $scope.show = true;

        $scope.getAccounts = function() {
            $http.get("/parabank/services/bank/customers/${customerId}/accounts")
                .then(function(response) {
                    $scope.accounts = response.data;
                    $scope.accounts.selectedOpton = $scope.accounts[0];
                });
        }

        $scope.getTypes = function() {
            $scope.types = parseTypes();
            $scope.types.selectedOption = $scope.types[0];
        }

        $scope.submit = function() {
            var url = "services/bank/createAccount?customerId=${customerId}&newAccountType="+ $scope.types.selectedOption.id + "&fromAccountId=" + $scope.accounts.selectedOpton.id;
            $http.post(url)
                .then(function(response) {
                    $scope.show = false;
                    $scope.newAccountId = response.data.id;
                    $scope.newAccountUrl = "/parabank/activity.htm" + "?id=" + $scope.newAccountId;
                });
        }

        function parseTypes() {
            var types = "${types}".replace("[", "").replace("]", "").split(", ");
            var i;
            var typesWithId = [];
            for(i=0; i<types.length; i++){
                typesWithId.push({id: i, name: types[i]});
            }
            return typesWithId;
        }
    });
</script>