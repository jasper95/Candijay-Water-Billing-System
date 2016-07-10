$(document).ready(function(){
    if($('#reading-year').val() === '')
        $('#reading-month').val(new Date().getFullYear());
    if($('#reading-month').val() === '')
        $('#reading-month').val(new Date().getMonth()+1);
    function showAccount(){
        $.post($('#fetchAccount').attr('action')+'/fetchAccount', $('#fetchAccount').serialize(), function(response){
            displayData(response);
            $('#crt-mr-found').show();
        });
    }
    showAccount();
    function displayData(response){
        var reading = response.reading;
        var account = response.account;
        var fullname =  account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
        var address = account.address.brgy+",  Zone "+account.address.locationCode;
        var lastReading = "Last Reading:  "+response.device.lastReading;
        var status = $('#status');
        if(account.status === "ACTIVE"){
            status.removeClass().addClass("label label-success");
        } else status.removeClass().addClass("label label-danger");
        status.text(account.status);
        $('#full-name').text(fullname);
        $('#address').text(address);
        $('#last-reading').text(lastReading);
        $("tbody",'#recent-readings').remove();
        if(reading === null){
            var row = "<tr><td colspan=\"4\"><center>No data available</center></td></tr>";
            $('#recent-readings').append(row);
        }
        else {
            var schedule = reading.schedule.monthSymbol+" "+reading.schedule.year;
            var row = "<tr><td>"+schedule+"</td><td>"+reading.consumption+"</td><td>"+reading.readingValue+"</td><td>"+reading.invoice.status+"</td></tr>";
            $('#recent-readings').append(row);
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
                if(response.status === "SUCCESS"){
                    $('#reading-success').html("Reading successfully updated");
                    showAccount();
                    $('#reading-success').show().delay(3000).fadeOut();
                }else{
                    var errorInfo = "";
                    for(i =0 ; i < response.result.length ; i++){
                        errorInfo += "<br>" + (i + 1) +". " + response.result[i].code;
                    }
                    $('#reading-error').html('<br><h4>You got an error!</h4><hr>' + errorInfo);
                    $('#reading-success').hide();
                    $('#reading-error').show().delay(3000).fadeOut();
                }
             }
        });
    });
});