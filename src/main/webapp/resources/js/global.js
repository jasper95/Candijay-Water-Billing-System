$(document).ready(function(){
    $('#md-name-edit-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-name-edit-form');
        $.post(form.attr('action'), form.serialize(), function(response){
            if(response.status === "FAILURE")
                showFieldError($('#fn'), response.reason)
            else showSuccess('#md-name-edit-form', "Fullname successfully updated")
        });
    });
    $('#md-password-edit').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#md-password-edit');
        $.post(form.attr('action'), form.serialize(), function(response){
            if(response.status === "FAILURE"){
                var errors = response.errors;
                if(errors.current !== undefined)
                    showFieldError($('#current-pw'), errors.current);
                if(errors.new !== undefined)
                    showFieldError($('#new-pw'), errors.new);
            }else showSuccess('#md-name-edit-form', "Password successfully updated");
        })
    });
    $('#update-profile-link').click(function(){
        cleanUpFormMsgs('#md-name-edit-form');
        cleanUpFormMsgs('#md-password-edit');
        cleanUpFormFields('#md-password-edit');
        $('#rt-profile-container .input-group').append('<span id="rt-new-pw-x" style="display:none" class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
        $('#update-profile-form-modal').modal('show');
    });
    $('#update-profile-link2').click(function(){
        $('#update-profile-link').trigger('click');
    });
    $('input:password.pw-update').keyup(function(){
        var rtPw =  $('#rt-new-pw').val();
        var pw = $('#new-pw').val();
        if(rtPw.length > 0 && rtPw != pw){
            if(!$('#rt-profile-container').hasClass('has-error')){
                $('#rt-profile-container').addClass('has-error');
                $('#rt-new-pw-x').show();
                $('#update-submit').prop('disabled', true);
            }
        } else {
            $('#rt-profile-container').removeClass('has-error');
            $('#rt-new-pw-x').hide();
            $('#update-submit').prop('disabled', false);
        }
    });
    $("[data-hide]").on("click", function(){
        $(this).closest("." + $(this).attr("data-hide")).hide();
    });
    $('ul .dropdown-menu a').on('click', function (event) {
        event.stopPropagation();
    });

    $.ajaxSetup({
       statusCode : {
           405 : function(){
               BootstrapDialog.alert({
                   title: 'Session Expired',
                   message: 'You were logged out somewhere or you were logged out due to inactivity. Please login again.',
                   type: BootstrapDialog.TYPE_DANGER,
                   callback : function(){
                       window.location.reload();
                   }
               });
               console.log("global")
           }
       }
    })
});