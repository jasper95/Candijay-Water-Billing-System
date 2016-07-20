/**
 * Created by Bert on 7/17/2016.
 */
$(document).ready(function(){
    window.displayData = function (response){
        var account = response.account;
        var fullname =  account.customer.firstName+ " "+ account.customer.middleName+" "+ account.customer.lastname;
        var address = account.address.brgy+",  Zone "+account.address.locationCode;
        var lastReading = "Last Reading:  "+response.last_reading;
        var status = $('#status');
        if(account.status === "ACTIVE"){
            status.removeClass().addClass("label label-success");
        } else status.removeClass().addClass("label label-danger");
        status.text(account.status);
        $('#full-name').text(fullname);
        $('#address').text(address);
        $('#last-reading').text(lastReading);
        $('#yadcf-filter--reading-0').val(account.id);
        $('#filterButton').click();
    }
    window.showAccount = function (data){
        if (data == undefined) {
            data = $('#fetchAccount').serialize();
        }
        var header = $("meta[name='_csrf_header']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");
        $.ajax({
            url : $('#fetchAccount').attr('action')+'/fetchAccount',
            data : data,
            type : "POST",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(response){
                if(response.status === "SUCCESS"){
                    displayData(response);
                    $('#crt-mr-found').show();
                }
            }
        });
    }
})