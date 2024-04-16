<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div>
	<div id="updateProfileForm">
		<h1 class="title"><fmt:message key="update.profile"/></h1>

		<form name="contact">
		  <table class="form2">
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="first.name"/>:</b></td>
			  <td width="20%">
				<input id="customer.firstName" name="customer.firstName" class="input" type="text">
			  </td>
			  <td width="50%">
				<span id="firstName-error" class="error" style="display:none"><fmt:message key="error.first.name.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="last.name"/>:</b></td>
			  <td width="20%">
				<input id="customer.lastName" name="customer.lastName" class="input" type="text">
			  </td>
			  <td width="50%">
				<span id="lastName-error" class="error" style="display:none"><fmt:message key="error.last.name.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="address"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.street" name="customer.address.street" class="input" type="text">
			  </td>
			  <td width="50%">
				<span id="street-error" class="error" style="display:none"><fmt:message key="error.address.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="city"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.city" name="customer.address.city" class="input" type="text">
			  </td>
			  <td width="50%">
				<span id="city-error" class="error" style="display:none"><fmt:message key="error.city.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="state"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.state" name="customer.address.state" class="input" type="text">
			  </td>
			  <td width="50%">
				<span id="state-error" class="error" style="display:none"><fmt:message key="error.state.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="zip.code"/>:</b></td>
			  <td width="20%">
				<input id="customer.address.zipCode" name="customer.address.zipCode" class="input" type="text">
			  </td>
			  <td width="50%">
				<span id="zipCode-error" class="error" style="display:none"><fmt:message key="error.zip.code.required"/></span>
			  </td>
			</tr>
			<tr>
			  <td align="right" width="30%"><b><fmt:message key="phone.number"/>:</b></td>
			  <td width="20%">
				<input id="customer.phoneNumber" name="customer.phoneNumber" class="input" type="text">
			  </td>
			  <td width="50%">
			  </td>
			</tr>
			<tr>
			  <td>&nbsp;</td>
			  <td colspan="2"><input type="button" class="button" value="<fmt:message key="update.profile"/>"></td>
			</tr>
		  </table>
		  <br>
		</form>
	</div>
	<div id="updateProfileResult" style="display:none">
		<h1 class="title"><fmt:message key="profile.updated"/></h1>
		<p><fmt:message key="your.updated.address"/></p>
	</div>

	<div id="updateProfileError" style="display:none">
		<h1 class="title"><fmt:message key="error.heading" /></h1>
		<p class="error"><fmt:message key="error.internal" /></p>
	</div>
</div>

<script>
	$(document).ready(function() {
		var customer = null;
		var showForm = function(visible) {
			if (visible) {
				$("#updateProfileForm").show();
			} else {
				$("#updateProfileForm").hide();
			}
		}
		var showResult = function(visible) {
			if (visible) {
				$("#updateProfileResult").show();
			} else {
				$("#updateProfileResult").hide();
			}
		}
		var showError = function() {
			if (visible) {
				$("#updateProfileError").show();
			} else {
				$("#updateProfileError").hide();
			}
		}
		var showError = function(error) {
            showForm(false);
            showResult(false);
            $('#updateProfileError').show();
            var status = error.status > 0 ? error.status : "timeout";
            var data = error.data ? error.data : "Server timeout"
            console.error("Server returned " + status + ": " + data);
        }
		var getCustomer = function() {
			$.ajax({
				url: "services_proxy/bank/customers/${customerId}",
				dataType: "json",
				success: function(data) {
					customer = data;
					$('#customer\\.firstName').val(data.firstName);
					$('#customer\\.lastName').val(data.lastName);
					$('#customer\\.address\\.street').val(data.address.street);
					$('#customer\\.address\\.city').val(data.address.city);
					$('#customer\\.address\\.state').val(data.address.state);
					$('#customer\\.address\\.zipCode').val(data.address.zipCode);
					$('#customer\\.phoneNumber').val(data.phoneNumber);
				},
				error: function(data) {
					showError(data);
				}
			})
		}
		var validate = function() {
			$('#customer\\.firstName').val() ? $('#firstName-error').hide() : $('#firstName-error').show();
			$('#customer\\.lastName').val() ? $('#lastName-error').hide() : $('#lastName-error').show();
			$('#customer\\.address\\.street').val() ? $('#street-error').hide() : $('#street-error').show();
			$('#customer\\.address\\.city').val() ? $('#city-error').hide() : $('#city-error').show();
			$('#customer\\.address\\.state').val() ? $('#state-error').hide() : $('#state-error').show();
			$('#customer\\.address\\.zipCode').val() ? $('#zipCode-error').hide() : $('#zipCode-error').show();
			
			//look thru validation markers to see if any are visible
        	return $("span.error:visible").length == 0;
		}
		var submit = function() {
			if (validate()) {
				var url = "services_proxy/bank/customers/update/${customerId}" +
					"?firstName=" + encodeURIComponent($('#customer\\.firstName').val()) +
					"&lastName=" + encodeURIComponent($('#customer\\.lastName').val()) +
					"&street=" + encodeURIComponent($('#customer\\.address\\.street').val()) +
					"&city=" + encodeURIComponent($('#customer\\.address\\.city').val()) +
					"&state=" + encodeURIComponent($('#customer\\.address\\.state').val()) +
					"&zipCode=" + encodeURIComponent($('#customer\\.address\\.zipCode').val()) +
					"&phoneNumber=" + encodeURIComponent($('#customer\\.phoneNumber').val()) +
					"&ssn=" + encodeURIComponent(customer.ssn) +
					"&username=${username}&password=${password}";
				$.ajax({
					url: url,
					type: "POST",
					timeout: 30000, 
					success: function(data) {
						//Will always fail if the update succeeds because
						//its trying to parse the response as JSON
 					},
					error: function(data) {
						// the server will return an error event if the update succeeds
						// check status code
						if (data.status == 200) {
							showForm(false);
							showResult(true);
						} else {
							showError(data);							
						}
					}
				})
			}
		}
		
		$("input[type=button]").click(() => {
        	submit(); 
		});
		getCustomer();
		showForm(true);
	});
</script>