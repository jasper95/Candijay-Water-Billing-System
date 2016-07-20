$(document).ready(function(){
    $('#md-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-update-form')
        $.post($('#reading-uri').val()+'update', form.serialize(), function(response){
            if(validateForm('#md-update-form', response)){
                showSuccess('#md-update-form', "Reading successfully updated")
                var reading = response.result;
                $('#reading tr:nth-child('+$('#row-num').val()+') td:nth-child(5)').text(reading.schedule.year);
                $('#reading tr:nth-child('+$('#row-num').val()+') td:nth-child(4)').text(months[reading.schedule.month]);
                $('#reading tr:nth-child('+$('#row-num').val()+') td:nth-child(8)').text(reading.consumption);
                $('#reading tr:nth-child('+$('#row-num').val()+') td:nth-child(9)').text(reading.readingValue);
            }
        })
    })
});