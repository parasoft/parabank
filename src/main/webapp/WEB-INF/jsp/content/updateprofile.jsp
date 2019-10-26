<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div ng-app="UpdateProfileApp" ng-controller="UpdateProfileCtrl" ng-cloak>
	<div ng-if="showForm" ng-init="getCustomer()">
		<h1 class="title"><fmt:message key="update.profile"/></h1>

		<form name="contact" ng-submit="submit()">
		  <table class="form2">
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="first.name"/>:</b></td>
			  <td width="20%">
				<input id="customer.firstName" name="customer.firstName" class="input" type="text" ng-model="customer.firstName">
			  </td>
			  <td width="50%">
				<span ng-if="customer && !customer.firstName" class="error"><fmt:message key="error.first.name.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="last.name"/>:</b></td>
			  <td width="20%">
				<input id="customer.lastName" name="customer.lastName" class="input" type="text" ng-model="customer.lastName">
			  </td>
			  <td width="50%">
				<span ng-if="customer && !customer.lastName" class="error"><fmt:message key="error.last.name.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="address"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.street" name="customer.address.street" class="input" type="text" ng-model="customer.address.street">
			  </td>
			  <td width="50%">
				<span ng-if="customer && !customer.address.street" class="error"><fmt:message key="error.address.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="city"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.city" name="customer.address.city" class="input" type="text" ng-model="customer.address.city">
			  </td>
			  <td width="50%">
				<span ng-if="customer && !customer.address.city" class="error"><fmt:message key="error.city.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="state"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.state" name="customer.address.state" class="input" type="text" ng-model="customer.address.state">
			  </td>
			  <td width="50%">
				<span ng-if="customer && !customer.address.state" class="error"><fmt:message key="error.state.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="zip.code"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.zipCode" name="customer.address.zipCode" class="input" type="text" ng-model="customer.address.zipCode">
			  </td>
			  <td width="50%">
				<span ng-if="customer && !customer.address.zipCode" class="error"><fmt:message key="error.zip.code.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="phone.number"/>:</b></td>
			  <td width="20%">
				<input id="customer.phoneNumber" name="customer.phoneNumber" class="input" type="text" ng-model="customer.phoneNumber">
			  </td>
			  <td width="50%">
			  </td>
			</tr>
			<tr>
			  <td>&nbsp;</td>
			  <td colspan="2"><input type="submit" class="button" value="<fmt:message key="update.profile"/>"></td>
			</tr>
		  </table>
		  <br>
		</form>
	</div>
	<div ng-if="showResult">
		<h1 class="title"><fmt:message key="profile.updated"/></h1>
		<p><fmt:message key="your.updated.address"/></p>
	</div>

	<div ng-if="showError">
		<h1 class="title"><fmt:message key="error.heading" /></h1>
		<p class="error"><fmt:message key="error.internal" /></p>
	</div>
</div>

<script>
    var app = angular.module('UpdateProfileApp', []);
    app.controller('UpdateProfileCtrl', function($scope, $http) {
        $scope.showForm = true;
        $scope.showResult = false;
        $scope.showError = false;

        $scope.getCustomer = function() {
            $http.get("services_proxy/bank/customers/${customerId}", {timeout:30000})
                .then(function(response) {
                    $scope.customer = response.data;
                })
                .catch(function(response) {
                    showError(response);
                });
        }

        $scope.submit = function() {
			if(validate()){
				var url = "services_proxy/bank/customers/update/${customerId}"
				 + "?firstName=" + encodeURIComponent($scope.customer.firstName)
				 + "&lastName=" + encodeURIComponent($scope.customer.lastName)
				 + "&street=" + encodeURIComponent($scope.customer.address.street)
				 + "&city=" + encodeURIComponent($scope.customer.address.city)
				 + "&state=" + encodeURIComponent($scope.customer.address.state)
				 + "&zipCode=" + encodeURIComponent($scope.customer.address.zipCode)
				 + "&phoneNumber=" + encodeURIComponent($scope.customer.phoneNumber)
				 + "&ssn=" + encodeURIComponent($scope.customer.ssn)
				 + "&username=${username}&password=${password}";
				$http.defaults.transformResponse = [];
				$http.post(url, "", {timeout:30000})
					.then(function(response) {
						$scope.showForm = false;
						$scope.showResult = true;
					})
					.catch(function(response) {
						showError(response);
					});
			}
        }
		
		function validate(){
			var customer = $scope.customer;
			return customer.firstName && customer.lastName && customer.address 
			&& customer.address.street && customer.address.city 
			&& customer.address.state && customer.address.zipCode;
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