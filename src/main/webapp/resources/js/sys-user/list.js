$(document).ready(function(){
    $('#user-form').on('submit', function(e){
        e.preventDefault();
        if($('#rt-pw').val() != $('#pw').val()){
            alert("Re-type password correctly first!");
            return false;
        }
        var form = $(this);
        cleanUpFormMsgs('#user-form');
        $.post($('#request-uri').val()+'/save', form.serialize(), function(response){
            if(validateForm('#user-form', response)){
                showSuccess('#user-form', "User successfully created!");
                $('#reload-table').trigger('click')
            }
        })
    });
    $('#user-update-form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#user-update-form');
        $.post($('#request-uri').val()+'/update', form.serialize(), function(response){
           if(validateForm('#user-update-form', response)){
               showSuccess('#user-update-form', "User successfully updated")
               var user = response.user;
               var row = $('#users tbody tr:nth-child('+$('#row-num').val()+')');
               row.find('.status').text(user.status);
               row.find('.type').text(user.type);
               $('#upd-vs').val(user.version);
           }
        });
    });

    $('input:password.pw-create').keyup(function(){
        var rtPw =  $('#rt-pw').val();
        var pw = $('#pw').val();
        if(rtPw.length > 0 && rtPw != pw){
            if(!$('#rt-container').hasClass('has-error')){
                $('#rt-container').addClass('has-error');
                $('#rt-pw-x').show();
                $('#create-submit').prop('disabled', true);
            }
        } else {
            $('#rt-container').removeClass('has-error');
            $('#rt-pw-x').hide();
            $('#create-submit').prop('disabled', false);
        }
    });

    window.updateUser = function(id){
        $.getJSON($('#request-uri').val()+'/find/'+ id, function(response){
            if(response.status === "SUCCESS"){
                cleanUpFormFields('#user-update-form');
                cleanUpFormMsgs('#user-update-form');
                var user = response.user;
                var roles = user.roles;
                //var checkboxes = $('div.checkbox-container').find('input[name=roles]')
                for (var i=0; i<roles.length; i++)
                    $('#roles-item-'+roles[i].id).bootstrapToggle('on');
                if(user.status === 'ACTIVE')
                    $('#status-toggle').bootstrapToggle('on');
                else $('#status-toggle').bootstrapToggle('off');
                $('#upd-id').val(user.id);
                $('#upd-vs').val(user.version);
                $('#user-form').hide();
                $('#user-update-form').show();
                $('#user-action').text('Update');
                $('#user-form-modal').modal('show');
            }
        });
    }
    $('#add-user').on('click', function(){
        $('#user-action').text('Create');
        $('#user-form-modal').modal('show');
        $('#user-form').show();
        cleanUpFormFields('#user-form');
        cleanUpFormMsgs('#user-form');
        $('#user-update-form').hide();
        $('#rt-container .input-group').append('<span id="rt-pw-x" style="display:none" class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
    });
    $('#status-toggle').change(function(){
        var status = $('#upd-status')
        if($(this).prop('checked'))
            status.val('1')
        else status.val('0')
    });
    $("#users").on("click", "tr", function() {
        $('#row-num').val($(this).index()+1)
    });
})