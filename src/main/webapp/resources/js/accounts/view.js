function activate(device_id){
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: $('#activate-url').val()+device_id,
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
$(document).ready(function(){
    $('#add-device').submit(function(e){
        e.preventDefault();
        $.post($(this).attr('action'), $(this).serialize(), function(response){
            if(response.status === "SUCCESS")
                $('#reload').trigger('click');
            else{
                var errorInfo = "";
                for(i =0 ; i < response.errors.length ; i++){
                    errorInfo += "<br>" + (i + 1) +". " + response.errors[i].defaultMessage;
                }
                $('#form-error').html('<br><h4>You got an error!</h4><hr>' + errorInfo);
                $('#form-success').hide('slow');
                $('#form-error').show('slow').delay(5000).fadeOut()
            }
        });
    });
});