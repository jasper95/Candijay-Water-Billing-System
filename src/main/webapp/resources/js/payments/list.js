$(document).ready(function(){
    $('#master-checkbox').click(function(e) {
        var chk = $(this).prop('checked');
        $('input', oTable_payment.$('tr', {"filter": "applied"} )).prop('checked',chk);
    });
    $('#apply').on('click', function(e){
        e.preventDefault();
        var form = $('#form');
        $.post(form.attr('action')+'/print-check', form.serialize(), function(response){
            if(response.status === "SUCCESS")
                openReport('POST', form.attr('action')+'/history', response.result, '_blank');
        })
    });
    $('#fn-payment-btn').on('click', function(){
        cleanUpFormMsgs('#fn-pm-form');
        cleanUpFormFields('#fn-pm-form');
        $('#finalize-payments-form-modal').modal('show');
    })
    $('#fn-pm-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#fn-pm-form');
        $.post($('#payments-uri').val()+"finalize-payments", form.serialize(), function(response){
            if(validateForm('#fn-pm-form', response)){
                var sucess_message = "Payments for "+response.barangay+" successfully finalized";
                if(response.status === "PENDING"){
                    confirmFinalize(response);
                }
                else showSuccess('#fn-pm-form', "Payments successfully finalized");
            }
        })
    })
    function confirmFinalize(response){
        var data = $('#fn-pm-form').serializeArray(), action = $('#payments-uri').val()+"finalize-payments";
        data.push({name: 'confirmed', value: 1});
        var warning_message = 'The due date for '+response.barangay+' on this month has not passed yet. Do you want to continue anyway?';
        BootstrapDialog.confirm({
            title: 'WARNING',
            message: warning_message,
            type: BootstrapDialog.TYPE_WARNING,
            closable: true,
            draggable: true,
            btnCancelLabel: 'Cancel',
            btnOKLabel: 'Continue',
            btnOKClass: 'btn-warning',
            callback: function(result) {
                if(result) {
                    $.post(action, data, function(response){
                        if(validateForm('#fn-pm-form', response)){
                            var message = "Payments for "+response.barangay+" successfully finalized";
                            showSuccess('#fn-pm-form', message);
                        }
                    })
                }
            }
        });
    }
});