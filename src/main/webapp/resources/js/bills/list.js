$(document).ready(function(){
    $('#master-checkbox').click(function(e) {
        var chk = $(this).prop('checked');
        $('input', oTable_invoice.$('tr', {"filter": "applied"} )).prop('checked',chk);
    });
    $('#apply').on('click', function(e){
        e.preventDefault();
        var form = $('#form');
        $.post(form.attr('action')+'/print-check', form.serialize(), function(response){
            if(response.status === "SUCCESS")
                openReport('POST', form.attr('action')+'/print', response.result, '_blank');
        })
    });
    window.editDisc = function(id){
        $.getJSON($('#form').attr('action')+"/find/"+id, function(response){
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
                $('#dc-id').val(invoice.id)
                $('#dc-val').val(invoice.discount);
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
    $('#discount-form').on('submit', function(e){
        e.preventDefault();
        $.post($('#form').attr('action')+'/update-discount', $(this).serialize(), function(response){
            cleanUpFormMsgs('#discount-form');
            if(validateForm('#discount-form', response)){
                showSuccess('#discount-form', "Discount successfully updated.");
                var invoiceDue = "Invoice Total Due: &#8369 "+response.invoice.netCharge;
                $('#bill-dc-due').html(invoiceDue);
                var row = $('#invoice tbody tr:nth-child('+$('#row-num').val()+')');
                row.find('.discount').html('&#8369 '+response.invoice.discount);
                row.find('.total-due').html('&#8369 '+response.invoice.netCharge);
            }
        })
    });
    $("#invoice").on("click", "tr", function() {
        $('#row-num').val($(this).index()+1)
    });
});