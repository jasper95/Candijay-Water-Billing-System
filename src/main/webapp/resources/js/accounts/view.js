$(document).ready(function(){
    window.activate = function (device_id){
        var header = $("meta[name='_csrf_header']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");
        $.ajax({
            url: $('#all-acct-base-url').val()+'/activate-device',
            data: {"device_id":device_id},
            type: "POST",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(response){
                if(response.status === "SUCCESS")
                    $('#reload').trigger('click');
            }
        });
    }
    $('#acct-form').on('submit', function(e){
        e.preventDefault();
        $.post($('#acct-base-url').val()+'/update', $(this).serialize(), function(response){
            cleanUpFormMsgs('#acct-form')
            if (validateForm('#acct-form', response)) {
                cleanUpFormMsgs('#acct-form');
                $('#acct-bg').val(response.result.address.brgy)
                $('#acct-lc').val(response.result.purok)
                showSuccess('#acct-form', "Account successfully updated")
            }
        });
    });
    $('#device-form').on('submit', function (e){
        e.preventDefault();
        $.post($('#device-action-url').val(), $(this).serialize(), function (response) {
            cleanUpFormMsgs('#device-form');
            if (validateForm('#device-form', response)) {
                var action;
                if($('#device-action').text() == 'Add')
                    action = "added"
                else action = "updated"
                showSuccess('#device-form', "Device successfully "+action)
                $('#reload').trigger('click');
            }
        });
    });
    $('#update-acct-btn').click(function(){
        cleanUpFormMsgs('#acct-form')
        $('#acct-form-modal').modal('show');
    });
    $('#btn-add-dv').click(function (){
        $('#device-action').empty().append('Add');
        $('#device-form-modal').modal('show');
        cleanUpFormFields('#device-form')
        cleanUpFormMsgs('#device-form')
        $('#device-action-url').val($('#acct-base-url').val()+'/create-device')
    });
    window.editDv = function(device_id){
        cleanUpFormMsgs('#device-form')
        populateDeviceForm(device_id)
        $('#device-action').empty().append('Update');
        $('#device-form-modal').modal('show');
        $('#device-action-url').val($('#all-acct-base-url').val()+'/'+device_id+'/edit-device')
    }
    function populateDeviceForm(device_id){
        var header = $("meta[name='_csrf_header']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");
        $.ajax({
            url: $('#all-acct-base-url').val()+'/find-device',
            data: {'device_id': device_id},
            type: "POST",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(response){
                if(response == null)
                    alert("This device might have been deleted. Please reload the page.")
                else{
                    $('#acc-mc').val(response.meterCode)
                    $('#acc-mb').val(response.brand)
                }
            }
        });
    }
});