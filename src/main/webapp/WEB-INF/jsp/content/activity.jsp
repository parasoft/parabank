<%@ include file="../include/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div>
  <div id="accountDetails" style="display: none;">
    <h1 class="title"><fmt:message key="account.details" /></h1>
    <table>
      <tr>
        <td align="right"><fmt:message key="account.number" />:</td>
        <td id="accountId"></td>
      </tr>
      <tr>
        <td align="right"><fmt:message key="account.type" />:</td>
        <td id="accountType"></td>
      </tr>
      <tr>
        <td align="right"><fmt:message key="account.balance" />:</td>
        <td id="balance"></td>
      </tr>
      <tr>
        <td align="right"><fmt:message key="account.available.balance" />:</td>
        <td id="availableBalance"></td>
      </tr>
    </table>
    <br />
  </div>

  <div id="accountActivity" style="display: none;">
    <h1 class="title"><fmt:message key="account.activity" /></h1>
    <form id="activityForm">
      <table class="form_activity">
        <tr>
          <td align="right"><b><fmt:message key="activity.period" />:</b></td>
          <td>
            <select id="month" name="month" class="input">
              <c:forEach items="${months}" var="month">
                <option value="${month}">${month}</option>
              </c:forEach>
            </select>
          </td>
        </tr>
        <tr>
          <td align="right"><b><fmt:message key="transaction.type" />:</b></td>
          <td>
            <select id="transactionType" name="transactionType" class="input">
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

    <p id="noTransactions"><b><fmt:message key="no.transactions.found" /></b></p>

    <table id="transactionTable" class="gradient-style">
      <thead>
        <tr>
          <th><fmt:message key="date" /></th>
          <th><fmt:message key="transaction" /></th>
          <th><fmt:message key="debit" /></th>
          <th><fmt:message key="credit" /></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
  
  <div id="error" style="display: none;">
    <h1 class="title"><fmt:message key="error.heading" /></h1>
    <p class="error"><fmt:message key="error.internal" /></p>
  </div>
</div>

<script type="text/javascript">
  $(document).ready(function() {
    function fetchAccountDetails() {
      $.ajax({
        url: "services_proxy/bank/accounts/" + ${model.accountId},
        timeout: 30000,
        success: function(data) {
          $('#accountId').text(data.id);
          $('#accountType').text(data.type);
          $('#balance').text(formatCurrency(data.balance));
          $('#availableBalance').text(formatCurrency(data.balance < 0 ? 0 : data.balance));
        },
        error: function(xhr, status, error) {
          reportError(xhr.status > 0 ? xhr.status : "timeout", xhr.responseText ? xhr.responseText : "Server timeout");
        }
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

    function fetchAccountActivity(period, type) {
      $.ajax({
        url: "services_proxy/bank/accounts/" + ${model.accountId} + "/transactions/month/" + period + "/type/" + type,
        timeout: 30000,
        success: function(data) {
          $('#transactionTable tbody').empty();
          if (data.length > 0) {
        	$('#transactionTable').show();
            $('#noTransactions').hide();
            $.each(data, function(index, transaction) {
              $('#transactionTable tbody').append(
                "<tr>" +
                  "<td>" + formatDate(transaction.date) + "</td>" +
                  "<td><a href='transaction.htm?id=" + transaction.id + "'>" + transaction.description + "</a></td>" +
                  "<td>" + (transaction.type == 'Debit' ? formatCurrency(transaction.amount) : '') + "</td>" +
                  "<td>" + (transaction.type == 'Credit' ? formatCurrency(transaction.amount) : '') + "</td>" +
                "</tr>"
              );
            });
          } else {
            $('#noTransactions').show();
            $('#transactionTable').hide();
          }
        },
        error: function(xhr, status, error) {
          reportError(xhr.status > 0 ? xhr.status : "timeout", xhr.responseText ? xhr.responseText : "Server timeout");
        }
      });
    }
    
    function reportError(status, data) {
        $('#accountDetails').hide();
        $('#accountActivity').hide();
        $('#error').show().find('.error').text("Server returned " + status + ": " + data);
        console.error("Server returned " + status + ": " + data);
      }
    
    function fetchActivity() {
   	  var period = $('#month').val();
      var type = $('#transactionType').val();
      fetchAccountActivity(period, type);
    }

    $('#activityForm').submit(function(event) {
      event.preventDefault();
      fetchActivity()
    });
    
    $('#accountDetails').show();
    $('#accountActivity').show();
    fetchAccountDetails();
    fetchActivity()
  });
  

</script>
