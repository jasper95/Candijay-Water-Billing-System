$(document).ready(function(){
    window.editDisc = function(id){
        $.getJSON($('#bills-uri').val()+"edit-discount/"+id, function(response){
            if(response.status === "SUCCESS"){
                cleanUpFormMsgs('#discount-form');
                var invoice = response.invoice, account = invoice.account;
                var fullname = account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
                var address = account.address.brgy+",  Zone "+account.address.locationCode;
                var invoiceDue = "Invoice Total Due: &#8369 "+invoice.netCharge;
                var invoiceSched = invoice.schedule.monthSymbol+" "+invoice.schedule.year;
                $('#bill-dc-full-name').text(fullname);
                $('#bill-dc-address').text(address);
                $('#bill-dc-due').html(invoiceDue);
                $('#bill-dc-date').text(invoiceSched);
                $('#dc-id').val(invoice.id);
                $('#dc-val').val(invoice.discount);
                $('#bill-due-reference').val(invoice.netCharge+invoice.discount);
                $('#bill-discount-form-modal').modal('show');
            } else{
                BootstrapDialog.alert({
                    title: 'ACTION DENIED',
                    message: 'You can edit accounts\' latest unpaid invoice only.',
                    type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
                    closable: true, // <-- Default value is false
                    draggable: true, // <-- Default value is false
                });
            }
        });
    };
    $('#dc-val').on('keyup', function(){
        realTimeCalculationHelper($(this), $('#bill-due-reference'), $('#bill-dc-due'),"Invoice Total Due: &#8369 ");
    });
    $('#discount-form').on('submit', function(e){
        e.preventDefault();
        $.post($('#bills-uri').val()+'update-discount', $(this).serialize(), function(response){
            cleanUpFormMsgs('#discount-form');
            if(validateForm('#discount-form', response)){
                showSuccess('#discount-form', "Discount successfully updated.");
                var row = $('#reading tbody tr:first');
                $('#fetchAccount').submit();
            }
        })
    });
});