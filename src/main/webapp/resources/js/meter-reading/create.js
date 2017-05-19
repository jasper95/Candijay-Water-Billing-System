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
        var submitButton = $('#cr-reading-submit'), origText = submitButton.text();
        changeButtonState(submitButton, true, '');
        $.ajax({
            url: $(this).attr('action'),
            data:$(this).serialize(),
            type:"POST",
            success: function(response){
                cleanUpFormMsgs('#add-meterReading-form');
                if (validateForm('#add-meterReading-form', response)) {
                    showSuccess('#add-meterReading-form', "Reading successfully added")
                    cleanUpFormFields('#add-meterReading-form');
                    $('#last-reading').html("Last Reading:  "+response.result.readingValue);
                    $('#acc-no').find('input:first').val(response.result.account.id);
                    $('#filterButton').trigger('click');
                    searchAgain();
                }
                changeButtonState(submitButton, false, origText);
             }
        });
    });
    setMonth();
    $('#activate-acct-btn').on('click', function(){
        var action = $('#accounts-uri').val(), data = $('#activate-account-form').serialize(), status = $('#status');
        $.post(action+'activate-account', data, function (response){
            if(response.status === "SUCCESS"){
                status.text("ACTIVE");
                status.removeClass().addClass("label label-success");
                $('#activate-acct-btn').hide();
            }
        });
    });
    $('#md-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-update-form');
        var submitButton = $('#up-reading-submit'), origText = submitButton.text();
        changeButtonState(submitButton, true, '');
        $.post($('#reading-uri').val()+'update', form.serialize(), function(response){
            if(validateForm('#md-update-form' ,response)){
                showSuccess('#md-update-form', "Reading successfully updated")
                var reading = response.result;
                $('#filterButton').trigger('click');
                $('#last-reading').html("Last Reading:  "+reading.readingValue);
                $('#rd-vs').val(reading.version); //in case user submits the form again without closing the modal
            }
            changeButtonState(submitButton, false, origText);
        })
    });
    $('#readingVal').on('keyup', function(){
        var reading = parseInt($(this).val());
        if(!isNaN(reading)){
            var lastReading = parseInt($('#last-reading-reference').val());
            var consumption = reading - lastReading;
            $('#reading-consumption').text(consumption);
        } else $('#reading-consumption').text(0);

    });
});