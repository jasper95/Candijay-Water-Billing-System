$(document).ready(function(){
    $('#add-payment-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        $.post($('#payments-uri').val()+'new', form.serialize(), function(response){
            cleanUpFormMsgs('#add-payment-form');
            if (validateForm('#add-payment-form', response)){
                showSuccess('#add-payment-form', "Payment successfully added");
                cleanUpFormFields('#add-payment-form');
                var status = $('#status');
                var account = response.result.account;
                if(account.status === "ACTIVE")
                    status.removeClass().addClass("label label-success");
                else status.removeClass().addClass("label label-danger");
                status.text(account.status);
                $('#last-reading').html("Standing Balance: &#8369; "+ account.accountStandingBalance);
                $('#acc-no').find('input:first').val(account.id);
                $('#filterButton').trigger('click');
                searchAgain();
            }
        })
    });
    $("#date").datepicker({ maxDate : new Date, changeMonth: true, changeYear: true, yearRange : '-6:+0', dateFormat: 'yy/mm/dd'});
    $("#date").datepicker("setDate", new Date());
    $('#or-num').attr('maxlength', 7);
    $('#amount-paid').on('keyup', function(){
        realTimeCalculationHelper($(this), $('#acc-balance-val'), $('#last-reading'), "Balance: &#8369; ");
    });
});