$(document).ready(function(){
    if($('#reading-year').val() === '')
        $('#reading-year').val(new Date().getFullYear());
    if($('#reading-month').val() === '')
        $('#reading-month').val(new Date().getMonth()+1);
    $('#fetchAccount').on('submit', function(e){
        e.preventDefault();
        cleanUpFormMsgs('#add-meterReading-form')
        $('#readingVal').val('');
        $.ajax({
            data:$(this).serialize(),
            type:"POST",
            url:$(this).attr('action')+'/fetchAccount',
            success: function(response){
                cleanUpFormMsgs('#fetchAccount')
                if(validateForm('#fetchAccount', response)){
                    $('#accountId').val(response.account.id);
                    displayData(response);
                    $('#crt-mr-found').show();
                } else $('#crt-mr-found').hide();
            }
        });
    });
    $('#add-meterReading-form').on('submit', function(e){
        e.preventDefault();
        $('#reading-error').hide();
        $.ajax({
            url: $(this).attr('action'),
            data:$(this).serialize(),
            type:"POST",
            success: function(response){
                cleanUpFormMsgs('#add-meterReading-form')
                if (validateForm('#add-meterReading-form', response)) {
                    showSuccess('#add-meterReading-form', "Reading successfully added")
                    cleanUpFormFields('#add-meterReading-form')
                    showAccount({accountNumber: response.result.account.number, create: true})
                }
             }
        });
    });
});