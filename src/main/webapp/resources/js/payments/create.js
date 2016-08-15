$(document).ready(function(){
    $('#add-payment-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this)
        $.post($('#payments-uri').val()+'save', form.serialize(), function(response){
            cleanUpFormMsgs('#add-payment-form')
            if (validateForm('#add-payment-form', response)){
                showSuccess('#add-payment-form', "Payment successfully added")
                cleanUpFormFields('#add-payment-form')
                $('#acc-no').find('input:first').val(response.result.account.id);
                $('#filterButton').trigger('click');
            }
        })
    })
    $("#date").datepicker({ dateFormat: 'yy/mm/dd'});
});