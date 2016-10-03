$(document).ready(function(){
    $('#md-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-update-form');
        $.post($('#reading-uri').val()+'update', form.serialize(), function(response){
            if(validateForm('#md-update-form', response)){
                showSuccess('#md-update-form', "Reading successfully updated");
                var reading = response.result;
                var row = $('#reading tbody tr:nth-child('+$('#row-num').val()+')');
                row.find('.year').text(reading.schedule.year);
                row.find('.month').text(months[reading.schedule.month]);
                row.find('.consumption').text(reading.consumption);
                row.find('.reading').text(reading.readingValue);
                $('#rd-vs').val(reading.version);
            }
        })
    });
});