/**
 * Created by Bert on 7/17/2016.
 */
$(document).ready(function(){
    $('#fetchAccount').on('submit', function(e){
        e.preventDefault();
        cleanUpFormMsgs('#add-meterReading-form')
        cleanUpFormMsgs('#add-payment-form')
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
    window.displayData = function (response){
        var account = response.account;
        var fullname =  account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
        var address = account.address.brgy+",  Zone "+account.address.locationCode;
        var lastReading = (response.last_reading !== undefined) ? "Last Reading:  "+response.last_reading : "Last Due: &#8369; "+response.lastDue ;
        var status = $('#status');
        if(account.status === "ACTIVE"){
            status.removeClass().addClass("label label-success");
        } else status.removeClass().addClass("label label-danger");
        status.text(account.status);
        $('#full-name').text(fullname);
        $('#address').text(address);
        $('#last-reading').html(lastReading);
        $('#acc-no').find('input:first').val(account.id);
        $('#filterButton').click();
    }
});