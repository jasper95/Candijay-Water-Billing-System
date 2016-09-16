$(document).ready(function(){
    $('#master-checkbox').click(function(e) {
        var chk = $(this).prop('checked');
        $('input', oTable_account.$('tr', {"filter": "applied"} )).prop('checked',chk);
    });
    $('#apply').on('click', function(){
        var action = $('#action').val(), form = $('#form');
        if(action === '1'){
            $.post(form.attr('action')+'//notice-of-disconnection-check', form.serialize(), function(response){
                if(response.status === "SUCCESS"){
                    openReport('POST',form.attr('action')+"/print-notice-of-disconnection",response.result,'_blank');
                } else showError("No account is qualified for the action");
            });
        }
        else if (action === "2")
            changeStatus(form.serialize(), form.attr('action')+'/deactivate-accounts');
        else if(action === "3")
            changeStatus(form.serialize(), form.attr('action')+'/warning-accounts');
        else if(action === "4")
            changeStatus(form.serialize(), form.attr('action')+'/activate-accounts');

    });
    function changeStatus(data, url){
        $.post(url, data, function(response){
            if(response.status === "SUCCESS") {
                BootstrapDialog.alert({
                    title: 'ACTION SUCCESS',
                    message: "Action successfully performed",
                    type: BootstrapDialog.TYPE_SUCCESS, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
                    closable: true, // <-- Default value is false
                    draggable: true, // <-- Default value is false
                });
                $('#filterButton').trigger('click');
            }
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