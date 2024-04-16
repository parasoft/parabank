<%@ include file="../include/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div>
	<div id="formContainer">
		<h1 class="title">
			<fmt:message key="find.transactions" />
		</h1>
		<form id="transactionForm">
			<div>
				<b><fmt:message key="select.an.account" /></b>: <select
					id="accountId" class="input">
					<c:forEach items="${accounts}" var="account">
						<option value="${account}">${account}</option>
					</c:forEach>
				</select> <span id="accountIdError" class="error"></span>
			</div>

			<hr />

			<div>
				<b><fmt:message key="find.by.transaction.id" /></b>: <input
					id="transactionId" class="input" /> <span id="transactionIdError"
					class="error"></span>
			</div>

			<br />

			<div>
				<button type="submit" class="button" id="findById">
					<fmt:message key="find.transactions" />
				</button>
			</div>

			<hr />

			<div>
				<b><fmt:message key="find.by.date" /></b>: <input id="transactionDate"
					class="input" /> (
				<fmt:message key="date.format" />
				) <span id="transactionDateError" class="error"></span>
			</div>

			<br />

			<div>
				<button type="submit" class="button" id="findByDate">
					<fmt:message key="find.transactions" />
				</button>
			</div>

			<hr />

			<div>
				<p>
					<b><fmt:message key="find.by.date.range" /></b>
				</p>
				<div>
					<fmt:message key="between" />
					<input id="fromDate" class="input" />
					<fmt:message key="and" />
					<input id="toDate" class="input" /> (
					<fmt:message key="date.format" />
					) <span id="dateRangeError" class="error"></span>
				</div>
			</div>

			<br />

			<div>
				<button type="submit" class="button" id="findByDateRange">
					<fmt:message key="find.transactions" />
				</button>
			</div>

			<hr />

			<div>
				<b><fmt:message key="find.by.amount" /></b>: <input id="amount"
					class="input" /> <span id="amountError" class="error"></span>
			</div>

			<br />

			<div>
				<button type="submit" class="button" id="findByAmount">
					<fmt:message key="find.transactions" />
				</button>
			</div>

		</form>
	</div>

	<div id="resultContainer" style="display: none;">
		<h1 class="title">
			<fmt:message key="transaction.results" />
		</h1>
		<table id="transactionTable" class="gradient-style">
			<thead>
				<tr>
					<th><fmt:message key="date" /></th>
					<th><fmt:message key="transaction" /></th>
					<th><fmt:message key="debit" /></th>
					<th><fmt:message key="credit" /></th>
				</tr>
			</thead>
			<tbody id="transactionBody">
			</tbody>
		</table>
	</div>

	<div id="errorContainer" style="display: none;">
		<h1 class="title">
			<fmt:message key="error.heading" />
		</h1>
		<p class="error">
			<fmt:message key="error.internal" />
		</p>
	</div>

</div>

<script>
	$(document).ready(
			function() {
				
				function submitCriteria(criteria) {
					var accountId = $('#accountId').val();
				    var url = 'services_proxy/bank/accounts/' + accountId + '/transactions/';
				    if (criteria.searchType === 'DATE') {
				        url += 'onDate/' + criteria.onDate;
				    } else if (criteria.searchType === 'DATE_RANGE') {
				        url += 'fromDate/' + criteria.fromDate + '/toDate/' + criteria.toDate;
				    } else if (criteria.searchType === 'AMOUNT') {
				        url += 'amount/' + criteria.amount;
				    }
				    $.get(url, { timeout: 30000 })
				        .then(function(response) {
				        	$('#formContainer').hide();
							$('#resultContainer').show();
							displayTransactions(response);
				        })
				        .catch(function(error) {
				        	$('#formContainer').hide();
							$('#resultContainer').hide();
							$('#errorContainer').show();
							console.error("Server returned " + status
									+ ": " + error);
				        });
				}

				function displayTransactions(transactions) {
					var transactionBody = $('#transactionBody');
					transactionBody.empty();
					transactions.forEach(function(transaction) {
						var formattedDate = formatDate(transaction.date);
						var transactionRow = $('<tr>');
						transactionRow.append($('<td>').text(formattedDate));
						transactionRow.append($('<td>').html(
								'<a href="${pageContext.request.contextPath}/transaction.htm?id='
										+ transaction.id + '">'
										+ transaction.description + '</a>'));
						transactionRow.append($('<td>').text((transaction.type == 'Debit' ? formatCurrency(transaction.amount) : '')));
						transactionRow.append($('<td>').text((transaction.type == 'Credit' ? formatCurrency(transaction.amount) : '')));
						transactionBody.append(transactionRow);
					});
				}
				

				function formatDate(dateString) {
					var date = new Date(dateString);
					var month = date.getMonth() + 1;
					var day = date.getDate();
					var year = date.getFullYear();
					var formattedDate = (month < 10 ? '0' : '') + month + '-' + (day < 10 ? '0' : '') + day + '-' + year;
					return formattedDate;
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
				
				$("#findById").on("click", function(event) {
					event.preventDefault();
					var criteria = {};
				    var transactionId = $("#transactionId").val();
				    if (!isNaN(parseInt(transactionId)) && isFinite(transactionId)) {
				        criteria.searchType = "ID";
				        criteria.transactionId = parseInt(transactionId);
				        submitCriteria(criteria);
				    } else {
				        $("#transactionIdError").text("Invalid transaction ID");
				    }
				});
				
				$("#findByDate").on("click", function(event) {
					event.preventDefault();
					var criteria = {};
				    var date = $("#transactionDate").val();
				    if (isValidDate(date)) {
				        criteria.searchType = "DATE";
				        criteria.onDate = date;
				        submitCriteria(criteria);
				    } else {
				        $("#transactionDateError").text("Invalid date format");
				    }
				});

				$("#findByDateRange").on("click", function(event) {
					event.preventDefault();
					var criteria = {};
				    var fromDate = $("#fromDate").val();
				    var toDate = $("#toDate").val();
				    if (isValidDate(fromDate) && isValidDate(toDate)) {
				        criteria.searchType = "DATE_RANGE";
				        criteria.fromDate = fromDate;
				        criteria.toDate = toDate;
				        submitCriteria(criteria);
				    } else {
				        $("#dateRangeError").text("Invalid date format");
				    }
				});

				$("#findByAmount").on("click", function(event) {
					event.preventDefault();
					var criteria = {};
				    var amount = $("#amount").val();
				    if (!isNaN(parseFloat(amount)) && isFinite(amount)) {
				        criteria.searchType = "AMOUNT";
				        criteria.amount = parseFloat(amount);
				        submitCriteria(criteria);
				    } else {
				        $("#amountError").text("Invalid amount");
				    }
				});

				function isValidDate(dateString) {
				    var regex = /^\d{2}-\d{2}-\d{4}$/;
				    return regex.test(dateString);
				}
			});
</script>
