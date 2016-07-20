$(document).ready(function(){
    if($('#reading-year').val() === '')
        $('#reading-month').val(new Date().getFullYear());
    if($('#reading-month').val() === '')
        $('#reading-month').val(new Date().getMonth()+1);
    showAccount();
    $('#add-meterReading-form').on('submit', function(e){
        e.preventDefault();
        $('#reading-error').hide();
        $.ajax({
            url: $(this).attr('action'),
            data:$(this).serialize(),
            type:"POST",
            success: function(response){
                if(response.status === "SUCCESS"){
                    $('#reading-success').html("Reading successfully updated");
                    showAccount();
                    $('#reading-success').show().delay(3000).fadeOut();
                }else{
                    var errorInfo = "";
                    for(i =0 ; i < response.result.length ; i++){
                        errorInfo += "<br>" + (i + 1) +". " + response.result[i].code;
                    }
                    $('#reading-error').html('<br><h4>You got an error!</h4><hr>' + errorInfo);
                    $('#reading-success').hide();
                    $('#reading-error').show().delay(3000).fadeOut();
                }
             }
        });
    });
});