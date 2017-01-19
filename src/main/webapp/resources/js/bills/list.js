$(document).ready(function(){
    $('#master-checkbox').click(function(e) {
        var chk = $(this).prop('checked');
        $('input', oTable_invoice.$('tr', {"filter": "applied"} )).prop('checked',chk);
    });
    $('#apply').on('click', function(e){
        e.preventDefault();
        var form = $('#checkbox-form');
        var action = $('#bills-uri').val();
        $.post(action+'print-check', form.serialize(), function(response){
            if(response.status === "SUCCESS")
                openReport('POST', action+'print', response.result, '_blank');
        })
    });
    $('#dc-val').on('keyup', function(){
        realTimeCalculationHelper($(this), $('#bill-due-reference'), $('#bill-dc-due'),"Invoice Total Due: &#8369 ");
    });
    $('#discount-form').on('submit', function(e){
        e.preventDefault();
        $.post($('#bills-uri').val()+'update-discount', $(this).serialize(), function(response){
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
    window.billPrintablePreview = function(id){
        openReport('POST', $('#bills-uri').val()+'preview', {id:id}, '_blank')
    };
});