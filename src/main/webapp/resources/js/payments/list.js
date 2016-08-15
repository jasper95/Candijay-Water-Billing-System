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
                showSuccess('#fn-pm-form', "Payments successfully finalized");
            }
        })
    })
});