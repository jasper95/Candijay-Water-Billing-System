$(document).ready(function(){
    $('#master-checkbox').click(function(e) {
        var chk = $(this).prop('checked');
        $('input', oTable_account.$('tr', {"filter": "applied"} )).prop('checked',chk);
    });
    $('#apply').on('click', function(){
        var action = $('#action').val(), form = $('#form');
        if(action === '1' || action === '2'){
            $.post(form.attr('action')+'/deactivate-check', form.serialize(), function(response){
                if(response.status === "SUCCESS"){
                    if(action === '1')
                        openReport('POST',form.attr('action')+"/print-notice-of-disconnection",response.result,'_blank');
                    else if( action  === '2')
                        changeStatus(form.serialize(), form.attr('action')+'/deactivate-accounts');
                } else showError("No account is qualified for the action");
            });
        }
        else if(action === "3")
            changeStatus(form.serialize(), form.attr('action')+'/warning-accounts');
        else if(action === "4")
            changeStatus(form.serialize(), form.attr('action')+'/activate-accounts');

    });
    function changeStatus(data, url){
        $.post(url, data, function(response){
            if(response.status === "SUCCESS")
                $('#filterButton').trigger('click');
            else showError("No account is qualified for the action");
        });
    }

    function showError(message){
        BootstrapDialog.alert({
            title: 'ACTION DENIED',
            message: message,
            type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
            closable: true, // <-- Default value is false
            draggable: true, // <-- Default value is false
        });
    }
});