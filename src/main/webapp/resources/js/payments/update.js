$(document).ready(function(){

    $("#pm-date").datepicker({ dateFormat: 'yy/mm/dd'});

    window.viewChanges = function(id){
        $('#auditPayment-id').find('input:first').val(id);
        $('#filterButtonAuditTable').trigger('click');
        $('#payment-info-modal').modal('show');
    };
    window.checkCanEdit = function (id) {
        $.getJSON($('#payments-uri').val()+ id + "/check-can-edit", function(data) {
            if(data.status === "SUCCESS"){
                cleanUpFormMsgs('#md-update-form');
                var payment = data.payment;
                var account = payment.account;
                $('#pm-paid').val(payment.amountPaid);
                $('#pm-discount').val(payment.discount);
                $('#pm-date').val(payment.date);
                $('#pm-version').val(payment.version);
                $('#pm-or').val(payment.receiptNumber)
                $('#ac-id').val(account.id);
                $('#pid').val(id);
                $('#cr-time-audit').text(payment.creationTime);
                $('#up-time-audit').text(payment.modificationTime);
                $('#cr-user-audit').text(payment.createdByUser);
                $('#up-user-audit').text(payment.modifiedByUser);
                var fullname = account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
                var address = account.address.brgy+",  Zone "+account.address.locationCode;
                var lastDue= "Last Due: &#8369; "+payment.invoice.netCharge;
                var status = $('#md-status');
                if(account.status === "ACTIVE"){
                    status.removeClass().addClass("label label-success");
                } else status.removeClass().addClass("label label-danger");
                status.text(account.status);
                $('#md-full-name').text(fullname);
                $('#md-address').text(address);
                $('#md-last-reading').html(lastDue);
                $('#payment-form-modal').modal('show');
            }
            else BootstrapDialog.alert({
                title: 'ACTION DENIED',
                message: 'You can only edit payments to latest invoice',
                type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
                closable: true, // <-- Default value is false
                draggable: true, // <-- Default value is false
            });
        });
    }
    $("#payment").on("click", "tr", function() {
        $('#row-num').val($(this).index()+1)
    });
    $('#md-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-update-form')
        $.post($('#payments-uri').val()+'save', form.serialize(), function(response){
            if(validateForm('#md-update-form' ,response)){
                showSuccess('#md-update-form', "Payment successfully updated")
                var payment = response.result;
                var invoice = payment.invoice;
                var row = $('#payment tbody tr:nth-child('+$('#row-num').val()+')');
                row.find('.invoice-status').text(invoice.status);
                row.find('.payment-discount').html('&#8369 '+payment.discount);
                row.find('.payment-amount').html('&#8369 '+payment.amountPaid);
                row.find('.payment-date').text(payment.date);
                row.find('.or-number').text(payment.receiptNumber);
                $('#pm-version').val(payment.version);
            }
        })
    });
});