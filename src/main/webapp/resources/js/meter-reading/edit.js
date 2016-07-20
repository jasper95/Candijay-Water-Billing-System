$(document).ready(function(){
    window.checkCanEdit = function (id, year, month, readingVal) {
        $.getJSON($('#reading-uri').val()+ id + "/check-paid", function(data) {
            if(data.status === "SUCCESS"){
                $('#reading-form-modal').modal('show');
                $('#rd-mn').val(month);
                $('#rd-yr').val(year);
                $('#reading-val').val(readingVal);
                $('#rd-id').val(id);
                var account = data.account;
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
    $('#md-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-update-form')
        $.post(($('#reading-uri').val()+'update', form.serialize, function(response){
            if(validateForm('#md-update-form' ,response)){
                showSuccess('#md-update-form', "Reading successfully updated")
                cleanUpFormFields('#md-update-form')
            }
        }))
    })
});