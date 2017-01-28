$(document).ready(function(){
    window.viewChanges = function(id){
        $('#auditReading-id').find('input:first').val(id);
        $('#filterButtonAuditTable').trigger('click');
        $('#reading-info-modal').modal('show');
    };
    window.checkCanEdit = function (id) {
        $.getJSON($('#reading-uri').val()+ id + "/check-can-edit", function(data) {
            if(data.status === "SUCCESS"){
                cleanUpFormMsgs('#md-update-form');
                var reading = data.reading;
                //populate form
                $('#rd-mn').val(reading.schedule.month);
                $('#rd-yr').val(reading.schedule.year);
                $('#reading-val').val(reading.readingValue);
                $('#rd-id').val(id);
                $('#ac-id').val(reading.account.id);
                $('#rd-vs').val(reading.version);
                $('#cr-time-audit-mr').text(reading.creationTime);
                $('#up-time-audit-mr').text(reading.modificationTime);
                $('#cr-user-audit-mr').text(reading.createdByUser);
                $('#up-user-audit-mr').text(reading.modifiedByUser);
                //populate account details
                var account = reading.account;
                var fullname = account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
                var address = "Purok "+account.purok+", "+account.address.brgy;
                var lastReading = "Last Reading:  "+data.last_reading;
                var status = $('#md-status');
                if(account.status === "ACTIVE"){
                    status.removeClass().addClass("label label-success");
                } else status.removeClass().addClass("label label-danger");
                status.text(account.status);
                $('#md-full-name').text(fullname);
                $('#md-address').text(address);
                $('#md-last-reading').text(lastReading);
                $('#reading-form-modal').modal('show');
            }
            else{
                BootstrapDialog.alert({
                    title: 'ACTION DENIED',
                    message: data.message,
                    type: BootstrapDialog.TYPE_WARNING,
                    closable: true,
                    draggable: true,
                });
            }
        });
    };
    $("#reading").on("click", "tr", function() {
        $('#row-num').val($(this).index()+1)
    });
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
    window.deleteItem = function (id){
        BootstrapDialog.show({
            title: 'CONFIRM DELETE',
            message: 'This action could not be undo in the future. Do you really want to delete this reading?',
            type: BootstrapDialog.TYPE_WARNING,
            closable: true,
            draggable: true,
            onshown: function(dialog) {
                dialog.getButton('delete-reading-ok').focus();
            },
            buttons : [
                {
                    label : 'Cancel',
                    action: function(dialog){
                        dialog.close();
                    }
                },
                {
                    id : 'delete-reading-ok',
                    label: 'Delete',
                    cssClass: 'btn-warning',
                    action : function(dialog){
                        dialog.close();
                        $.getJSON($('#reading-uri').val()+'delete/'+id, function(response){
                            if(response.status === "SUCCESS"){
                                $('#fetchAccount').submit();
                            } else {
                                BootstrapDialog.alert({
                                    title: 'ACTION DENIED',
                                    message: 'You cannot delete paid readings',
                                    type: BootstrapDialog.TYPE_DANGER,
                                    closable: true,
                                    draggable: true,
                                });
                            }
                        });
                    }
                }
            ]
        });
    };
});