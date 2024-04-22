<%@ include file="../include/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="transferApp">
	<div id="showForm">
		<h1 class="title">
			<fmt:message key="transfer.funds" />
		</h1>
		<p id="amount.errors" class="error" style="display: none;">
			<fmt:message key="error.amount.empty" />
		</p>
		<p id="amount.errors" class="error" style="display: none;">
			<fmt:message key="typeMismatch.java.math.BigDecimal" />
		</p>
		<form id="transferForm">
			<p>
				<b><fmt:message key="transfer.amount" />:</b> $<input id="amount"
					type="text" name="input" />
			</p>
			<div>
				<fmt:message key="from.account.number" />
				<select id="fromAccountId" class="input"></select>
				<fmt:message key="to.account.number" />
				<select id="toAccountId" class="input"></select>
			</div>
			<br />
			<div>
				<input type="submit" class="button"
					value="<fmt:message key="transfer"/>">
			</div>
		</form>
	</div>
	<div id="showResult" style="display: none;">
		<h1 class="title"><fmt:message key="transfer.complete" /></h1>
		<p>
			<fmt:message key="transfer.confirmation">
				<fmt:param value="<span id='amountResult'></span>" />
				<fmt:param value="<span id='fromAccountIdResult'></span>" />
				<fmt:param value="<span id='toAccountIdResult'></span>" />
			</fmt:message>
		</p>
		<p>
			<fmt:message key="see.account.activity" />
		</p>
	</div>
	<div id="showError" style="display: none;">
		<h1 class="title">
			<fmt:message key="error.heading" />
		</h1>
		<p class="error">
			<fmt:message key="error.internal" />
		</p>
	</div>
</div>

<script>
    $(document).ready(function() {
        function getAccounts() {
            $.ajax({
                url: "services_proxy/bank/customers/" + ${customerId} + "/accounts",
                type: "GET",
                timeout: 30000,
                success: function(response) {
                    var accounts = response;
                    $.each(accounts, function(index, account) {
                        $('#fromAccountId, #toAccountId').append($('<option>', {
                            value: account.id,
                            text: account.id
                        }));
                    });
                    $('#fromAccountId option:first-child').attr('selected', true);
                    $('#toAccountId option:first-child').attr('selected', true);
                    
                    $('#fromAccountId').change(function() {
                        var selectedValue = $(this).val();
                        $('#fromAccountId option').removeAttr('selected');
                        $('#fromAccountId option[value="' + selectedValue + '"]').attr('selected', 'selected');
                    });

					$('#toAccountId').change(function() {
						var selectedValue = $(this).val();
						$('#toAccountId option').removeAttr('selected');
						$('#toAccountId option[value="' + selectedValue + '"]').attr('selected', 'selected');
					});
                    
                },
                error: function(xhr, status, error) {
                    showError(xhr);
                }
            });
        }

        $('#transferForm').submit(function(event) {
            event.preventDefault();
            resetErrors();

            var amount = $('#amount').val();
            var fromAccountId = $('#fromAccountId').val();
            var toAccountId = $('#toAccountId').val();

            var url = "services_proxy/bank/transfer?fromAccountId=" + fromAccountId + "&toAccountId=" + toAccountId + "&amount=" + amount;

            $.ajax({
                url: url,
                type: "POST",
                timeout: 30000,
                dataType: 'text',
                success: function(response) {
                    $('#showForm').hide();
                    $('#showResult').show();
                    $('#amountResult').text(formatCurrency(amount));
                    $('#fromAccountIdResult').text(fromAccountId);
                    $('#toAccountIdResult').text(toAccountId);
                },
                error: function(xhr, status, error) {
                    showError(xhr);
                }
            });
        });

        function resetErrors() {
            $('#amount.errors').hide();
        }

        function showError(xhr) {
            $('#showForm, #showResult').hide();
            $('#showError').show();
            var status = xhr.status > 0 ? xhr.status : "timeout";
            var data = xhr.responseJSON ? xhr.responseJSON.message : "Server timeout";
            console.error("Server returned " + status + ": " + data);
        }
        
        function formatCurrency(amount) {
        	if (amount == null) {
        		return "$0.00";
        	}
            amount = parseFloat(amount);
            var isNegative = amount < 0;
            amount = Math.abs(amount);
            var formattedAmount = amount.toFixed(2);
            formattedAmount = (isNegative ? '-$' : '$') + formattedAmount;
            return formattedAmount;
        }
        
        getAccounts();
    });
</script>
