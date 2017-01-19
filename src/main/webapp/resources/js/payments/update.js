$(document).ready(function(){
    window.checkCanEdit = function (id) {
        $.getJSON($('#payments-uri').val()+ id, function(data) {
            if(data.status === "SUCCESS"){
                cleanUpFormMsgs('#md-update-form');
                var payment = data.payment;
                var account = payment.account;
                $('#pm-paid').val(payment.amountPaid);
                $('#pm-date').val(payment.date);
                $('#pm-version').val(payment.version);
                $('#pm-or').val(payment.receiptNumber);
                $('#ac-id').val(account.id);
                $('#pid').val(id);
                $('#cr-time-audit').text(payment.creationTime);
                $('#up-time-audit').text(payment.modificationTime);
                $('#cr-user-audit').text(payment.createdByUser);
                $('#up-user-audit').text(payment.modifiedByUser);
                var fullname = account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
                var address = "Purok "+account.purok+", "+account.address.brgy;
                var status = $('#md-status');
                if(account.status === "ACTIVE"){
                    status.removeClass().addClass("label label-success");
                } else status.removeClass().addClass("label label-danger");
                status.text(account.status);
                $('#md-full-name').text(fullname);
                $('#md-address').text(address);
                $('#payment-form-modal').modal('show');
            }
            else BootstrapDialog.alert({
                title: 'PAYMENT NOT FOUND',
                message: 'Payment does not exist or might have been deleted.',
                type: BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
                closable: true, // <-- Default value is false
                draggable: true, // <-- Default value is false
            });
        });
    };
    $("#payment").on("click", "tr", function() {
        $('#row-num').val($(this).index()+1)
    });
    $('#md-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-update-form');
        $.post($('#payments-uri').val()+'update', form.serialize(), function(response){
            if(validateForm('#md-update-form' ,response)){
                showSuccess('#md-update-form', "Payment successfully updated");
                var payment = response.result;
                var row = $('#payment tbody tr:nth-child('+$('#row-num').val()+')');
                row.find('.or-number').text(payment.receiptNumber);
                $('#pm-version').val(payment.version);
            }
        })
    });
    $('#pm-or').attr('maxlength',7);
});