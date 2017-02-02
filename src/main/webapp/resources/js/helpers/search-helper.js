$(document).ready(function(){
    $('#fetchAccount').on('submit', function(e){
        e.preventDefault();
        cleanUpFormMsgs('#add-meterReading-form');
        cleanUpFormMsgs('#add-payment-form');
        $('#readingVal').val('');
        $.ajax({
            data:$(this).serialize(),
            type:"POST",
            url:$(this).attr('action')+'/fetchAccount',
            success: function(response){
                cleanUpFormMsgs('#fetchAccount')
                if(validateForm('#fetchAccount', response)){
                    $('#accountId').val(response.account.id);
                    $('#activate-account-input').val(response.account.id);
                    $('#acc-no').find('input:first').val(response.account.id);
                    $('#recent-payments-account-id').find('input:first').val(response.account.id);
                    displayData(response);
                    $('#filterButton').click();
                    $('.reading-ready').show();
                    $('#search-results').hide();
                    $('#reading-consumption').text(0);
                    $('#readingVal').focus();
                    $('#amount-paid').focus();
                } else $('#crt-mr-found').hide();
            }
        });
    });
    $('a.list-filter-btn').on('click', function(){
        $('#back-results').trigger('click');
    });
    window.selectAccount = function(number){
        $('#acc-nb').val(number);
        $('#fetchAccount').submit();
    };
    window.displayData = function (response){
        var account = response.account;
        var fullname =  account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
        var address = "Purok "+account.purok+", "+account.address.brgy;
        var isReading = (response.last_reading !== undefined), lastReading;
        if(isReading){
            lastReading = "Last Reading:  "+response.last_reading;
            $('#last-reading-reference').val(response.last_reading);
        } else {
            lastReading = "Balance: &#8369; "+account.accountStandingBalance ;
            $('#acc-balance-val').val(account.accountStandingBalance);
        }
        var status = $('#status');
        if(account.status === "ACTIVE"){
            status.removeClass().addClass("label label-success");
            $('#activate-acct-btn').hide();
        } else{
            status.removeClass().addClass("label label-danger");
            $('#activate-acct-btn').show();
        }
        status.text(account.status);
        $('#full-name').text(fullname);
        $('#address').text(address);
        $('#last-reading').html(lastReading);
    };
    window.autoSelectAccountCallback = function (settings, start, end, max, total, pre){
        if(start === 1 && total === 1){
            var row = $('#account tbody tr:first');
            $('td:eq(0) a:first', row).trigger('click');
        }
        return "";
    };
    $('#back-results').on('click', function (){
        $('.reading-ready').hide();
        $('#search-results').show();
    });
    window.searchAgain = function(){
        var search_acc_num_field = $('#acct-no').find('input:first'), acct_num_pattern = new RegExp("^0[1-9]+(-\[0-9]{1,5})$"), search_acc_num_val = search_acc_num_field.val();
        if(acct_num_pattern.test(search_acc_num_val))
            search_acc_num_field.val(search_acc_num_val.substring(0,3));
        search_acc_num_field.focus();
    };
    window.billPrintablePreview = function(id){
        openReport('POST', $('#bills-uri').val()+'preview', {id:id}, '_blank')
    };
    $('#payment-print-preview-btn').on('click', function(e){
        e.preventDefault();
        var action = $('#payments-uri').val(), id = $('#acc-no').find('input:first').val();
        openReport('POST',action+"recent-payments", {id : id},'_blank');
    });
    $('#payment-history-btn').on('click', function(){
        $('#filterRecentPayments').click();
        $('#recent-payments-info-modal').modal('show');
    });
    window.viewBillDetails = function(id){
        $.getJSON($('#bills-uri').val()+id, function(data){
            if(data.status === "SUCCESS"){
                var reading = data.result, invoice = reading.invoice;
                populateBillDetails(reading, invoice);
            }
        }) ;
    };
    function populateBillDetails (reading, invoice){
        //schedule
        $('#bd-month').text(reading.schedule.monthSymbol);
        $('#bd-year').text(reading.schedule.year);
        $('#bd-due-date').text(invoice.dueDate);
        //consumption
        $('#bd-present').text(reading.readingValue);
        $('#bd-consumed').text(reading.consumption);
        $('#bd-previous').text(reading.readingValue-reading.consumption);
        //charges
        $('#bd-basic').text(invoice.basic);
        $('#bd-sys-loss').text(invoice.systemLoss);
        $('#bd-dep-fund').text(invoice.depreciationFund);
        $('#bd-pes').text(invoice.others);
        $('#bd-penalty').text(invoice.penalty);
        $('#bd-total-current1').text(invoice.totalCurrent);
        //summary
        $('#bd-arrears').text(invoice.arrears);
        $('#bd-total-current2').text(invoice.totalCurrent);
        $('#bd-discount').text(invoice.discount);
        $('#bd-total-due').text(invoice.netCharge);
        $('#bd-unpaid-due').text(invoice.remainingTotal);
        $('#bd-status').text(invoice.status);
    };
    window.autoClickLatestBillCallback = function (settings, start, end, max, total, pre){
        if(total < 1)
            $('#bill-details-panel').hide();
        else $('#bill-details-panel').show();
        if(start === 1){
            var row = $('#reading tbody tr:first');
            $('td:eq(1) a:first', row).trigger('click');
        }
        return "";
    };
    $("#readingVal, #date").keydown(function (e) {
        if (e.which == 9) {
            e.preventDefault();
            $('#acct-no').find('input:first').focus();
        }
    });

});