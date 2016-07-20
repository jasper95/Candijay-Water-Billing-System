$(document).ready(function(){
    window.checkCanEdit = function (id) {
        $.getJSON($('#reading-uri').val()+ id + "/check-paid", function(data) {
            if(data.status === "SUCCESS"){
                cleanUpFormMsgs('#md-update-form');
                var reading = data.reading;
                $('#reading-form-modal').modal('show');
                $('#rd-mn').val(reading.schedule.month);
                $('#rd-yr').val(reading.schedule.year);
                $('#reading-val').val(reading.readingVal);
                $('#rd-id').val(id);
                $('#ac-id').val(reading.account.id)
                var account = reading.account;
                var fullname = account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
                var address = account.address.brgy+",  Zone "+account.address.locationCode;
                var lastReading = "Last Reading:  "+data.last_reading;
                var status = $('#md-status');
                if(account.status === "ACTIVE"){
                    status.removeClass().addClass("label label-success");
                } else status.removeClass().addClass("label label-danger");
                status.text(account.status);
                $('#md-full-name').text(fullname);
                $('#md-address').text(address);
                $('#md-last-reading').text(lastReading);
            }
            else alert("You cannot edit a paid reading");
        });
    }
    $("#reading").on("click", "tr", function() {
        $('#row-num').val($(this).index()+1)
    });
});