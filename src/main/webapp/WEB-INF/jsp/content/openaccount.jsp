<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div>
  <div id="openAccountForm">
    <h1 class="title"><fmt:message key="open.new.account" /></h1>
    <form>
      <p><b><fmt:message key="what.type.of.account" /></b></p>
      <select id="type" class="input">
        <c:forEach items="${types}" var="type" varStatus="loop">
          <option value="${loop.index}">${type}</option>
        </c:forEach>
      </select>
      <br /><br />
      <fmt:formatNumber type="currency" value="${minimumBalance}" var="minValue" currencySymbol="$" maxFractionDigits="2" minFractionDigits="2" />
      <p><b><fmt:message key="minimum.deposit"><fmt:param value="${minValue}" /></fmt:message></b></p>
      <select id="fromAccountId" class="input"></select>
      <br /><br />
      <div><input type="button" class="button" value="<fmt:message key="open.new.account"/>"></div>
    </form>
  </div>

  <div id="openAccountResult" style="display:none">
    <h1 class="title"><fmt:message key="account.opened" /></h1>
    <p><fmt:message key="congratulations" /></p>
    <p><b><fmt:message key="new.account.number" />:</b> <a id="newAccountId" href=""></a></p>
  </div>

  <div id="openAccountError" style="display:none">
    <h1 class="title"><fmt:message key="error.heading" /></h1>
    <p class="error"><fmt:message key="error.internal" /></p>
  </div>
</div>

<script>
	$(document).ready(function() {
		var accounts = null;
		var types = [];
		var newAccountId = null;
		var newAccountUrl = null;
		var showForm = function(visible) {
			if (visible) {
				$("#openAccountForm").show();
			} else {
				$("#openAccountForm").hide();
			}
		}
		var showResult = function(visible) {
			if (visible) {
				$("#openAccountResult").show();
			} else {
				$("#openAccountResult").hide();
			}	
		}
		var showError = function(xhr) {
			showForm(false);
            showResult(false);
            $("#openAccountError").show();
            JumiBank.logAjaxError(xhr);
		}
		var getAccounts = function() {
			JumiBank.getJSON("/services_proxy/bank/customers/${customerId}/accounts")
				.done(function(data) {
					accounts = data;
					if (!accounts || !accounts.length) {
						return;
					}
					accounts.selectedOption = accounts[0];
					$("#fromAccountId").empty();
					$.each(accounts, function(i, item) {
						$("#fromAccountId").append($("<option></option>").attr("value", item.id).text(item.id));
					});
				})
				.fail(function(xhr) {
					showError(xhr);
				});
		}
		getAccounts();
		$('#fromAccountId').change(function() {
	        var accountId = $(this).val();
	        accounts.forEach((account) => {
	        	if (account.id == accountId) {
	        		accounts.selectedOption = account;
	        	}
	        })
	        
	    });
		$('#type').change(function() {
			var type = $(this).val();
			types.selectedOption = type;
		});
		types.selectedOption = $('#type').val();
		var submit = function() {
			if (!accounts || !accounts.length || !accounts.selectedOption) {
				return;
			}
			JumiBank.postJSON("/services_proxy/bank/createAccount?customerId=${customerId}&newAccountType="+ types.selectedOption + "&fromAccountId=" + accounts.selectedOption.id)
				.done(function(data) {
					newAccountId = data.id;
					newAccountUrl = JumiBank.url("/activity.htm?id=" + newAccountId);
					$('#newAccountId').attr("href", newAccountUrl).text(newAccountId);
					showForm(false);
					showResult(true);
				})
				.fail(function(xhr) {
					showError(xhr);
				});
		}
		$("input[type=button]").click(() => {
        	submit(); 
		});
	})
</script>