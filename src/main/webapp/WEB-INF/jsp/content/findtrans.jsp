<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="FindTransactionApp" ng-controller="FindTransactionCtrl" ng-cloak>

    <div ng-if="showForm">
    <h1 class="title"><fmt:message key="find.transactions"/></h1>
    <form ng-submit="submit()">
        <div>
            <b><fmt:message key="select.an.account"/></b>:
            <select class="input" ng-init="criteria.accountId = '${accounts[0]}'" ng-model="criteria.accountId">
                <c:forEach items="${accounts}" var="account">
                    <option value="${account}">${account}</option>
                </c:forEach>
            </select>
            <errors path="accountId" class="error"/>
        </div>
  
        <hr/>
  
        <div>
            <b><fmt:message key="find.by.transaction.id"/></b>: <input class="input" ng-model="criteria.transactionId" ng-required="criteria.searchType === 'ID'"/>
            <errors path="criteria.transactionId" class="error"/>
        </div>
        
        <br/>
        
        <div>
            <button type="submit" class="button" ng-click="criteria.searchType = 'ID'">
                <fmt:message key="find.transactions"/>
            </button>
        </div>
  
        <hr/>
  
        <div>
            <b><fmt:message key="find.by.date"/></b>: <input class="input" ng-model="criteria.onDate" ng-required="criteria.searchType === 'DATE'"/>
            (<fmt:message key="date.format"/>)
            <br/>
            <errors path="criteria.onDate" class="error"/>
        </div>
        
        <br/>
        
        <div>
            <button type="submit" class="button" ng-click="criteria.searchType = 'DATE'">
            <fmt:message key="find.transactions"/>
            </button>
        </div>
  
        <hr/>
  
        <div>
            <p><b><fmt:message key="find.by.date.range"/></b></p>
            <div>
                <fmt:message key="between"/>
                <input class="input" ng-model="criteria.fromDate" ng-required="criteria.searchType === 'DATE_RANGE'"/>
                <fmt:message key="and"/>
                <input class="input" ng-model="criteria.toDate" ng-required="criteria.searchType === 'DATE_RANGE'"/>
                (<fmt:message key="date.format"/>)
            </div>
        </div>
        
        <br/>
        
        <div>
            <button type="submit" class="button" ng-click="criteria.searchType = 'DATE_RANGE'">
                <fmt:message key="find.transactions"/>
            </button>
        </div>

        <hr/>
  
        <div>
            <b><fmt:message key="find.by.amount"/></b>: <input class="input" ng-model="criteria.amount" ng-required="criteria.searchType === 'AMOUNT'"/>
        </div>
        
        <br/>
        
        <div>
            <button type="submit" class="button" ng-click="criteria.searchType = 'AMOUNT'">
                <fmt:message key="find.transactions"/>
            </button>
        </div>
  
    </form>
    </div>
    
    <div ng-if="showResult">
        <h1 class="title"><fmt:message key="transaction.results"/></h1>
        <table id="transactionTable" class="gradient-style">
            <thead>
                <tr>
                    <th><fmt:message key="date"/></th>
                    <th><fmt:message key="transaction"/></th>
                    <th><fmt:message key="debit"/></th>
                    <th><fmt:message key="credit"/></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="transaction in transactions track by transaction.id">
                    <td>{{formatDate(transaction.date)}}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/transaction.htm?id={{transaction.id}}">{{transaction.description}}</a>
                    </td>
                    <td><span ng-if="transaction.type === 'Debit'">{{currencyFormat(transaction.amount)}}</span></td>
                    <td><span ng-if="transaction.type === 'Credit'">{{currencyFormat(transaction.amount)}}</span></td>
                </tr>
            </tbody>
        </table>
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
    var app = angular.module('FindTransactionApp', []);
    app.controller('FindTransactionCtrl', function($scope, $http) {

        $scope.criteria = { };
        $scope.showForm = true;
        $scope.showResult = false;
        $scope.showError = false;

        $scope.formatDate = function(dateStr) {
        	   var date = new Date(dateStr);
        	   var month = date.getMonth() + 1 + "";
        	   if (month.length === 1) {
        		   month = "0" + month;
           }
        	   return month + '-' + date.getDate() + '-' +  date.getFullYear();
        }
        
        $scope.currencyFormat = function(amount) {
        	   return '$' + amount.toFixed(2);
        }
        
        $scope.submit = function() {
        	   var url = ''
        	   if ($scope.criteria.searchType === 'ID') {
        		   url = 'services_proxy/bank/transactions/' + $scope.criteria.transactionId
        	   } else {
        		   url = 'services_proxy/bank/accounts/' + $scope.criteria.accountId + '/transactions/';
        		   if ($scope.criteria.searchType === 'DATE') {
        			   url += 'onDate/' + $scope.criteria.onDate;
        		   } else if ($scope.criteria.searchType === 'DATE_RANGE') {
        			   url += 'fromDate/' + $scope.criteria.fromDate + '/toDate/' + $scope.criteria.toDate;
        		   } else if ($scope.criteria.searchType === 'AMOUNT') {
        			   url += 'amount/' + $scope.criteria.amount;
        		   }
        	   }
        	   $http.get(url, {timeout:30000})
        	       .then(function(response) {
                   $scope.showForm = false;
                   $scope.showResult = true;
                   if (Array.isArray(response.data)) {
                    $scope.transactions = response.data;                	   
                   } else {
                	    $scope.transactions = [response.data];
                   }
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
    });
</script>