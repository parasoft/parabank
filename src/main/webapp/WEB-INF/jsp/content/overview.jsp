<%@ include file="../include/include.jsp"%>

<div id="overviewAccountsApp">
	<div id="showOverview">
		<h1 class="title">
			<fmt:message key="accounts.overview" />
		</h1>
		<table id="accountTable" class="gradient-style">
			<thead>
				<tr>
					<th><fmt:message key="account" /></th>
					<th><fmt:message key="balance" /></th>
					<th><fmt:message key="available.amount" /></th>
				</tr>
			</thead>
			<tbody></tbody>
			<tfoot>
				<tr>
					<td colspan="3"><fmt:message key="balance.note" /></td>
				</tr>
			</tfoot>
		</table>
	</div>

	<div id="showError" style="display: none">
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
    
    var $overviewTable = $('#accountTable tbody');
    var $showOverview = $('#showOverview');
    var $showError = $('#showError');

    $.ajax({
        url: "services_proxy/bank/customers/" + ${model.customerId} + "/accounts",
        type: "GET",
        timeout: 30000,
        success: function(response) {
            var accounts = response;
            var totalBalance = 0;
            
            $.each(accounts, function(index, account) {
                var availableBalance = account.balance < 0 ? 0 : account.balance;
                totalBalance += parseFloat(account.balance);
                
                var row = '<tr>' +
                            '<td><a href="activity.htm?id=' + account.id + '">' + account.id + '</a></td>' +
                            '<td>' + formatCurrency(account.balance) + '</td>' +
                            '<td>' + formatCurrency(availableBalance) + '</td>' +
                          '</tr>';
                          
                $overviewTable.append(row);
            });
            
            var totalRow = '<tr>' +
                              '<td align="right"><b><fmt:message key="total" /></b></td>' +
                              '<td><b>' + formatCurrency(totalBalance) + '</b></td>' +
                              '<td>&nbsp;</td>' +
                            '</tr>';
                            
            $overviewTable.append(totalRow);
            
            $showOverview.show();
        },
        error: function(xhr, status, error) {
            $showOverview.hide();
            $showError.show();
            console.error("Server returned " + status + ": " + error);
        }
    });
});
</script>
