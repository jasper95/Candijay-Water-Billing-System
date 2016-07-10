$(document).ready(function(){
    $('#payment-form').on('submit', function(e){
       e.preventDefault();
       $.post($(this).attr('action'), $(this).serialize(), function(response){
           if(response.status === "SUCCESS")
               paymentFormSuccess();
           else paymentFormError(response.result);
               
       });
    });
    function paymentFormSuccess(){
        $('#validation-success').html("Payment successfully updated");
        $("#validation-success").show().delay(3000).fadeOut();
        showAccount();
    }
    function paymentFormError(errors){
        var errorMsg = "";
        for(i=0;i<errors.length; i++){
            if(errors[i].code === "typeMismatch" || errors[i].code === "NotNull"){
                errorMsg += "<br>" + (i + 1) +".  Invalid " + getFieldError(errors[i].field);
            }
            else errorMsg += "<br>" + (i + 1) +". " + errors[i].code;
        }
        $('#validation-error').html('<br><h4>You got an error!</h4><hr>'+errorMsg);
        $("#validation-error").show().delay(3000).fadeOut();
    }
    function getFieldError(field){
       if(field === "payment.amountPaid"){
           return "amount paid";
       } else if(field === "payment.discount")
           return "discount";
    }
    function showAccount(){
        $.post($('#fetchAccount').attr('action')+'/fetchAccount', $('#fetchAccount').serialize(), function(response){
            displayData(response.invoice);
            $('#crt-mr-found').show();
        });
    }
    showAccount();
    function displayData(invoice){
        var account = invoice.account;
        var fullname =  account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
        var address = account.address.brgy+",  Zone "+account.address.locationCode;
        var status = $('#status');
        if(account.status === "ACTIVE"){
            status.removeClass().addClass("label label-success");
        } else status.removeClass().addClass("label label-danger");
        status.text(account.status);
        $('#full-name').text(fullname);
        $('#address').text(address);
        $("tbody",'#recent-readings').remove();
        var dueDate = invoice.dueDate.year+"/"+invoice.dueDate.monthOfYear+"/"+invoice.dueDate.dayOfMonth;
        var schedule = invoice.schedule.monthSymbol+" "+invoice.schedule.year;
        var row = "<tr><td>"+invoice.id+"</td><td>"+schedule+"</td><td>"+dueDate+"</td><td> &#8369;"+invoice.netCharge+"</td><td>"+invoice.status+"</td></tr>";
        $('#recent-readings').append(row);
    }
});