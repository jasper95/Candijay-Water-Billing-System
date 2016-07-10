$(document).ready(function(){
    $('#payment-form').on('submit', function(e){
       e.preventDefault();
       $.post($(this).attr('action'), $(this).serialize(), function(response){
           if(response.status === "SUCCESS")
               paymentFormSuccess();
           else paymentFormError(response.result);
               
       });
    });
    function clearPaymentForm(){
        $('#date').val('');
        $('#amount-paid').val('');
        $('#discount').val('');
    }
    
    function paymentFormSuccess(){
        clearPaymentForm();
        $('#fetchAccount').trigger('submit');
        $('#validation-success').html("Payment successfully added");
        $("#validation-success").show().delay(3000).fadeOut();
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
    $('#fetchAccount').on('submit', function(e){
        e.preventDefault();
        $('#search-error').hide();
        $('validation-error').hide();
        $("#validation-success").hide();
        clearPaymentForm();
        //$('tbody','#recent-readings').remove();
        $.ajax({
            data : $(this).serialize(),
            url : $(this).attr('action')+'/fetchAccount',
            type: "POST",
            success : function(response){
                if(response.account !== null){
                    $('#accountId').val(response.account.id);
                    displayData(response);                          
                    $('#crt-mr-found').show();         
                } else {
                    $('#crt-mr-found').hide();
                    $('#search-error').show().delay(3000).fadeOut();
                }
            }
        });
    });       
    function displayData(response){
        var invoice = response.invoice;
        var account = response.account;
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
        if(invoice === null){
            var row = "<tr><td colspan=\"5\"><center>No data available</center></td></tr>";
            $('#recent-readings').append(row);
        }
        else {
            var dueDate = invoice.dueDate.year+"/"+invoice.dueDate.monthOfYear+"/"+invoice.dueDate.dayOfMonth;
            var schedule = invoice.schedule.monthSymbol+" "+invoice.schedule.year;
            var row = "<tr><td>"+invoice.id+"</td><td>"+schedule+"</td><td>"+dueDate+"</td><td> &#8369;"+invoice.netCharge+"</td><td>"+invoice.status+"</td></tr>";
            $('#recent-readings').append(row);
        }
    }
});