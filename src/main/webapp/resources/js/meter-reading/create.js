$(document).ready(function(){
    function setMonth(){
        if($('#reading-year').val() === '' || $('#reading-month').val() === ''){
            var currentMonth = new Date().getMonth()+1, currentYear = new Date().getFullYear();
            if(currentMonth == 1){
                currentMonth = 12;
                currentYear--;
            } else {
                currentMonth--;
            }
            if($('#reading-year').val() === '')
                $('#reading-year').val(currentYear)
            if($('#reading-month').val() === '')
                $('#reading-month').val(currentMonth)
        }
    }
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
                    cleanUpFormFields('#add-meterReading-form');
                    $('#last-reading').html("Last Reading:  "+response.result.readingValue);
                    $('#acc-no').find('input:first').val(response.result.account.id);
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
                var row = $('#reading tbody tr:nth-child('+$('#row-num').val()+')')
                row.find('.schedule').text(months[reading.schedule.month]+" "+reading.schedule.year);
                row.find('.consumption').text(reading.consumption);
                row.find('.reading').text(reading.readingValue);
                $('#last-reading').html("Last Reading:  "+reading.readingValue);
                $('#rd-vs').val(reading.version);
            }
        })
    })
    setMonth();
});