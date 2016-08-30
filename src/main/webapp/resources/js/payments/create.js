$(document).ready(function(){
    $('#add-payment-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this)
        $.post($('#payments-uri').val()+'save', form.serialize(), function(response){
            cleanUpFormMsgs('#add-payment-form')
            if (validateForm('#add-payment-form', response)){
                showSuccess('#add-payment-form', "Payment successfully added");
                cleanUpFormFields('#add-payment-form');
                var status = $('#status');
                var account = response.result.account;
                if(account.status === "ACTIVE"){
                 status.removeClass().addClass("label label-success");
                 } else status.removeClass().addClass("label label-danger");
                 status.text(account.status);
                $('#acc-no').find('input:first').val(account.id);
                $('#filterButton').trigger('click');
            }
        })
    })
    $("#date").datepicker({ dateFormat: 'yy/mm/dd'});
});