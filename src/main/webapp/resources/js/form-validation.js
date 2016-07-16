function validateForm(form_id, response){
    var fields = [], errors = []
    var result = response.result
    if(response.status === 'FAILURE'){
        $(form_id+' input, '+form_id+' select').each(
            function(index){
                var input = $(this);
                if( (input.is('input') || input.is('select')) && name != undefined && name != '_csrf' )
                    fields[input.attr('name')] = input
            }
        );
        for(var i=0; i<result.length; i++)
            errors[result[i].field] = result[i].defaultMessage
        for(var error_field in errors){
            showError(fields[error_field], errors[error_field])
        }
        return false
    }
    return true
}

function showError(element, message){
    var errorHolder1 = element.parent()
    errorHolder1.append('<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>')
    var errorHolder2 = errorHolder1.parent()
    errorHolder2.addClass('has-error')
    errorHolder2.append('<span class="field-error">'+message+'</span>')
}

function cleanUpForm(){
    $('div.has-error').removeClass('has-error')
    $('span.field-error').remove()
    $('span.glyphicon-remove').remove()
}