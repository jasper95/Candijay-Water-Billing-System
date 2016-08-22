$(document).ready(function(){
    var current = null;
    var active;
    $('#summary-toggle').on('change',function(){
        $('#reportForm div.barangay').toggle();
        if($('#summary-status').val() == '0')
            $('#summary-status').val('1');
        else $('#summary-status').val('0')
    });
    $('#category').on('change', function(){
        cleanUpFormMsgs("#main-form");
        if (current !== null)
            $(active).hide();
        var value = $(this).val();
        var thereIsActive = true;
        if(value === "1")
            active = "#acctblityForm";
        else if(value === "2")
            active = ".report-form";
        else if(value === "3")
            active = "#chartForm";
        else thereIsActive = false;
        if(thereIsActive)
            current = $(active);
        else current = null;
        if(current !== null) {
            current.show();
            $('#submit').prop("disabled", false);
        } else $('#submit').prop("disabled", true);
    });
    $('#submit').on('click', function(e){
        e.preventDefault();
        var form, form_type, form_id = '#';
        var report_category = $('#category').val();
        if(report_category === "3"){
            form = $('#chartForm');
            form_type = 'chart';
        } else if (report_category === "2") {
            form = $('#reportForm');
            form_type = 'report';
        } else {
            form = $('#acctblityForm');
            form_type = 'accountability';
        }
        form_id += form.attr('id');
        cleanUpFormMsgs("#main-form");
        cleanUpFormMsgs(form_id);
        $.post($('#form-action').val()+'/validate-'+form_type+'-form', form.serialize(), function(response){
            if(validateForm(form_id ,response, "#main-form")) {
                showSuccess("#main-form", "The report was successfully generated.");
                openReport('POST', $('#form-action').val()+'/print-'+form_type, response.result, '_blank');
            }
        });
    });
});