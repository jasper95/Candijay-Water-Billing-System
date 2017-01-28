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
            }else{
                cleanUpFormFields('#md-password-edit');
                showSuccess('#md-name-edit-form', "Password successfully updated");
            }
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
    $('#rt-new-pw').keyup(function(){
        var rtPw =  $(this).val();
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
           403: function(){
               BootstrapDialog.alert({
                   title: 'Action Not Authorized',
                   message: 'You are not authorized for this action',
                   type: BootstrapDialog.TYPE_WARNING,
                   closable: true,
                   draggable: true
               });
           },
           405 : function(){
               BootstrapDialog.alert({
                   title: 'Session Expired',
                   message: 'You were logged out somewhere or you were logged out due to inactivity. Please login again.',
                   type: BootstrapDialog.TYPE_DANGER,
                   callback : function(){
                       window.location.reload();
                   }
               });
           }
       }
    });

    $('#search-filters').on("keypress", ".yadcf-filter", function(e){
        var code = e.keyCode || e.which;
        if(code==13)
            $('#filterButton').trigger('click');
    });
    $('#search-filters2').on("keypress", ".yadcf-filter", function(e){
        var code = e.keyCode || e.which;
        if(code==13)
            $('#filterButton2').trigger('click');
    });
    $('#acct-no').on("keydown", '.yadcf-filter', function (e){
        var input = $(this).val();
        var code = e.keyCode || e.which;
        if(input.length == 2 && code !== 8)
            $(this).val(input+"-");
    });
    $('.is-number').keydown(function (e) {
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
            (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) ||
            (e.keyCode >= 35 && e.keyCode <= 40)) {
            return;
        }
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });
     window.realTimeCalculationHelper = function(input, reference, target_display, text_display){
        var string_amount = input.val(), amount_paid = parseFloat(string_amount);
        amount_paid = !isNaN(amount_paid) ? amount_paid : 0;
        var current_balance = Number(parseFloat(reference.val())) ;
        if(current_balance >= amount_paid){
            var new_balance = current_balance.toFixed(2) - amount_paid.toFixed(2);
            target_display.html(text_display+new_balance.toFixed(2));
        }
        else
            input.val(string_amount.substring(0,string_amount.length-1));
    }
});