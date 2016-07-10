function customerUrl(data, type, full){
    return '<a href="customers/'+full.id+'"> '+ full.lastname+', '+full.firstName+' '+full.middleName+'</a>';
}
function customerListActions(data, type, full){
    return '<a type="button" href="customers/'+full.id+'/update" class="btn btn-ctm btn-xs btn-primary">Edit</a>'+
           ' <a type="button" href="accounts/'+full.id+'/new" class="btn btn-ctm btn-xs btn-primary">Add account</a>';
}
function accountUrl(data, type, full){
    return '<a href="'+$('#context-path').val()+'/admin/accounts/'+data+'"> '+data+'</a>';
}
function toPeso(data, type, full){
    return "&#8369; "+data;
}
function month(data, type, full){
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "June",
                "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
            ];
    return monthNames[data-1];
}
function checkbox(data, type, full){
    return '<input type="checkbox" name="checkboxValues" value="'+full.id+'" /> ';
}
function readingActions(data, type, full){
    return '<a type="button" onclick="checkCanEdit('+full.id+')" class="btn btn-ctm btn-xs btn-primary">Edit</a>';
}
function paidDate(data, type, full){
    return data.year+'/'+data.monthOfYear+'/'+data.dayOfMonth ;
}

function userListUsername(data, type, full){
    return (data === $('#current-user').val()) ? data+'(You)' : data;
}

function trueToYes(data, type, full){
    return (data) ? 'Yes' : 'No';
}

function activateDevice(data, type, full){
    return (full.active) ? '': '<a type="button" onclick="activate('+full.id+')" class="btn btn-ctm btn-xs btn-success">Activate</a>';
}

function activateAccount(data, type, full){
    //return (full.status === "ACTIVE" || full.status === "ACTIVE") ?  
    if(full.status === 'ACTIVE')
        return '<a type="button" onclick="warning('+full.id+')" class="btn btn-ctm btn-xs btn-danger">Warning</a>';
    else if(full.status === 'WARNING')
        return '<a type="button" onclick="deactivate('+full.id+')" class="btn btn-ctm btn-xs btn-danger">Deactivate</a>';
    else
        return '<a type="button" onclick="activate('+full.id+')" class="btn btn-ctm btn-xs btn-success">Activate</a>' ;
}

function updateUser(data, type, full){
    return (full.username === $('#current-user').val()) ? '' : '<a type="button" href="'+$('#request-uri').val()+'/update/'+full.username+'" class="btn btn-ctm btn-xs btn-primary">Edit</a>';
}