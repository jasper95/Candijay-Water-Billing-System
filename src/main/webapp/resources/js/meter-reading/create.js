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
                    $('#yadcf-filter--reading-0').val(response.result.account.id);
                    $('#filterButton').trigger('click');
                }
             }
        });
    });
    $('#md-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-update-form')
        $.post($('#reading-uri').val()+'update', form.serialize(), function(response){
            if(validateForm('#md-update-form' ,response)){
                showSuccess('#md-update-form', "Reading successfully updated")
                var reading = response.result;
                $('#reading tr:nth-child('+$('#row-num').val()+') td:nth-child(1)').text(months[reading.schedule.month]+" "+reading.schedule.year);
                $('#reading tr:nth-child('+$('#row-num').val()+') td:nth-child(2)').text(reading.consumption);
                $('#reading tr:nth-child('+$('#row-num').val()+') td:nth-child(3)').text(reading.readingValue)
            }
        })
    })
});