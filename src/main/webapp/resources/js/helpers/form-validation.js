function validateForm(form_id, response){
    var fields = [], field_errors = [], global_errors = []
    var result = response.result
    if(response.status === 'FAILURE'){
        $(form_id+' input, '+form_id+' select').each(
            function(index){
                var input = $(this);
                if( (input.is('input') || input.is('select')) && input.attr('name') != undefined && input.attr('name') != '_csrf' )
                    fields[input.attr('name')] = input
            }
        );
        for(var i=0; i<result.length; i++) {
            var field= result[i].field;
            if(field == undefined && result[i].code === 'global'){
                global_errors.push(result[i].defaultMessage);
            }
            else if(result[i].code === 'typeMismatch') {
                field_errors[field] = "Invalid input";
            } else if(field === 'roles'){ //some other input exceptions here
                global_errors.push(result[i].defaultMessage);
            }
            else field_errors[field] = result[i].defaultMessage
        }
        for(var error_field in field_errors){
            showFieldError(fields[error_field], field_errors[error_field])
        }
        if(global_errors.length > 0)
            showGlobalErrors(global_errors, form_id)
        return false
    }
    return true
}

function showFieldError(element, message){
    var errorHolder1 = element.parent()
    errorHolder1.append('<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>')
    var errorHolder2 = errorHolder1.parent()
    errorHolder2.addClass('has-error')
    if(message.length > 0)
        errorHolder2.append('<span class="field-error">'+message+'</span>')
}

function showGlobalErrors(errors, form_id){
    var errors_holder = $(form_id).find('div.global-errors')
    for(var i=0; i<errors.length; i++){
        errors_holder.append('<p class="hanging-indent"><i class="fa fa-remove fa-fw"></i>'+errors[i]+'</p>')
    }
    errors_holder.show()
}

function cleanUpFormMsgs(form_id){
    $(form_id).find('div.has-error').removeClass('has-error')
    $(form_id).find('span.field-error').remove()
    $(form_id).find('span.glyphicon-remove').remove()
    $(form_id).find('div.global-errors').hide();
    $(form_id).find('div.global-errors p').remove()
    $(form_id).find('div.success-msg').hide();
    $(form_id).find('div.success-msg p').remove()
}

function cleanUpFormFields(form_id){
    $(form_id+' input:not([type=hidden]), '+form_id+' select').each(function(index){
        var input = $(this);
        if(input.attr('name') != undefined && input.attr('name') != '_csrf' &&
            input.attr('name') != 'meterReading.schedule.month' && input.attr('name') != 'meterReading.schedule.year'){
            if(input.attr('type') === 'checkbox'){
               input.bootstrapToggle('off')
            } else {
                input.val('');
            }
        }

    })
}

function showSuccess(form_id, msg){
    $(form_id).find('div.success-msg').append('<p>'+msg+'</p>').show();
}