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
                clearCheckedRoles(form);
                cleanUpFormFields('#user-form');
                $('#reload-table').trigger('click')
            }
        });
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
               row.find('.type').text(user.typeToString);
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
                clearCheckedRoles($('#user-update-form'));
                cleanUpFormFields('#user-update-form');
                cleanUpFormMsgs('#user-update-form');
                var user = response.user;
                var roles = user.roles;
                for (var i=0; i<roles.length; i++) {
                    var checkboxRole = $('#roles-item-' + roles[i].id);
                    checkboxRole.bootstrapToggle('enable');
                    checkboxRole.bootstrapToggle('on');
                    checkboxRole.bootstrapToggle('disable');
                }
                if(user.status === 'ACTIVE')
                    $('#status-toggle').bootstrapToggle('on');
                else $('#status-toggle').bootstrapToggle('off');
                $('#upd-id').val(user.id);
                $('#upd-vs').val(user.version);
                $('#update-type-select').val(user.type);
                $('#user-form').hide();
                $('#user-update-form').show();
                $('#user-action').text('Update');
                $('#user-form-modal').modal('show');
            }
        });
    };
    $('#add-user').on('click', function(){
        $('#user-action').text('Create');
        $('#user-form-modal').modal('show');
        $('#user-form').show();
        clearCheckedRoles($('#user-form'));
        cleanUpFormFields('#user-form');
        cleanUpFormMsgs('#user-form');
        $('#user-update-form').hide();
        $('#rt-container .input-group').append('<span id="rt-pw-x" style="display:none" class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
    });
    $('#status-toggle').change(function(){
        var status = $('#upd-status')
        if($(this).prop('checked'))
            status.val('1');
        else status.val('0');
    });
    $('form select.type-select').change(function(){
        var type = $(this).val(), form = $(this).closest('form');
        clearCheckedRoles(form);
        if(type.length > 0){
            $.getJSON($('#request-uri').val()+'/get-allowed-roles/'+ type, function(response){
                if(response.status === "SUCCESS"){
                    var role_id_prefix = (form.attr('id') === 'user-form') ? "#add-user-roles-item-" : '#roles-item-';
                    for(var i=0; i < response.result.length; i++){
                        var optionToggle = $(role_id_prefix + response.result[i]);
                        optionToggle.bootstrapToggle('enable');
                        optionToggle.bootstrapToggle('on');
                        optionToggle.bootstrapToggle('disable');
                    }

                }
            });
        }
    });
    function clearCheckedRoles(form){
        $('input[name=roles]', form).each(function(){
            $(this).bootstrapToggle('enable');
            $(this).bootstrapToggle('off');
            $(this).bootstrapToggle('disable');
        });
    };
    function setStateRolesCheckbox(form, state){
        $('input[name=roles]', form).each(function(){
            $(this).bootstrapToggle(state);
        });
    };
    $("#users").on("click", "tr", function() {
        $('#row-num').val($(this).index()+1)
    });
});