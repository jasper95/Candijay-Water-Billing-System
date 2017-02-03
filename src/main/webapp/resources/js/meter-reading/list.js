$(document).ready(function(){
    window.viewChanges = function(id){
        $('#auditReading-id').find('input:first').val(id);
        $('#filterButtonAuditTable').trigger('click');
        $('#reading-info-modal').modal('show');
    };
    $('#acc-tbr').on('click', function(){
        cleanUpFormFields('#md-no-reading-info');
        $('#no-reading-info-modal').modal('show');
    });
    $('#isBrgy-toggle').on('change', function(){
        $('#md-no-reading-info div.barangay').toggle();
        $('#md-no-reading-info div.zone').toggle();
        if($('#isBrgy-param').val() === '0')
            $('#isBrgy-param').val('1');
        else
            $('#isBrgy-param').val('0');
    });
    $('#md-no-reading-info').on('submit', function(e){
        e.preventDefault();
        $('#brgy-prox').find('input:first').val($('#brgy-param').val());
        $('#zone-prox').find('input:first').val($('#zone-param').val());
        $('#isBrgy-prox').find('input:first').val($('#isBrgy-param').val());
        $('#filter-nr').trigger('click');
    });
    window.billPrintablePreview = function(id){
        openReport('POST', $('#bills-uri').val()+'preview', {id:id}, '_blank')
    };
});