$(document).ready(function(){
    $('#add-payment-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        BootstrapDialog.show({
            title: 'CONFIRM SAVE',
            message: 'Amount and Date Paid cannot be edited in the future. Are you sure all the Payment details is final?',
            type: BootstrapDialog.TYPE_WARNING,
            closable: true,
            draggable: true,
            onshown: function(dialog) {
                dialog.getButton('confirm-payment-ok').focus();
            },
            buttons : [
                {
                    label : 'Cancel',
                    action: function(dialog){
                        dialog.close();
                    }
                },
                {
                    id : 'confirm-payment-ok',
                    label: 'Pay',
                    cssClass: 'btn-warning',
                    action : function(dialog){
                        dialog.close();
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
                                $('#acc-balance-val').val(account.accountStandingBalance);
                                $('#acc-no').find('input:first').val(account.id);
                                $('#filterButton').trigger('click');
                                searchAgain();
                            }
                        });
                    }
                }
            ]
        });
    });
    $("#date").datepicker({ maxDate : new Date, changeMonth: true, changeYear: true, yearRange : '-6:+0', dateFormat: 'yy/mm/dd'});
    $("#date").datepicker("setDate", new Date());
    $('#or-num').attr('maxlength', 7);
    $('#amount-paid').on('keyup', function(){
        realTimeCalculationHelper($(this), $('#acc-balance-val'), $('#last-reading'), "Balance: &#8369; ");
    });
});