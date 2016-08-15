var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
];
function customerUrl(data, type, full){
    return '<a href="customers/'+full.id+'"> '+ full.lastname+', '+full.firstName+' '+full.middleName+'</a>';
}
function customerListActions(data, type, full){
    return '<a type="button" href="customers/'+full.id+'/update" class="btn btn-xs btn-primary">Edit</a>'+
           ' <a type="button" href="accounts/'+full.id+'/new" class="btn btn-xs btn-primary">Add account</a>';
}
function accountUrl(data, type, full){
    return '<a href="/admin/accounts/'+data+'"> '+data+'</a>';
}
function toPeso(data, type, full){
    if (data == null)
        return '---'
    else return "&#8369; "+data;
}
function month(data, type, full){
    return monthNames[data-1];
}
function checkbox(data, type, full){
    return '<input type="checkbox" name="checkboxValues" value="'+full.id+'" /> ';
}
function audit(data, type, full){
    if(full.version == 0)
        return '---';
    else return '<a type="button" onclick="viewChanges('+ full.id +')" class="btn btn-xs btn-primary"><i class="fa fa-info-circle fa-fw"></i></a>';
}
function auditPayment2(data, type, full){
    if(full.payment == null)
        return "---";
    else return '<a type="button" onclick="viewChanges('+ full.payment.id +')" class="btn btn-xs btn-primary"><i class="fa fa-info-circle fa-fw"></i></a>';
}
function readingActions(data, type, full){
    return '<a type="button" onclick="checkCanEdit('+ full.id +')" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>';
}
function paidDate(data, type, full){
    if(data == null)
        return '---'
    else return data.year+'/'+data.monthOfYear+'/'+data.dayOfMonth ;
}

function userListUsername(data, type, full){
    return (data === $('#current-user').val()) ? data+'(You)' : data;
}

function trueToYes(data, type, full){
    return (data) ? 'Yes' : 'No';
}

function activateDevice(data, type, full){
    return (full.active) ? '': '<a type="button" onclick="activate('+full.id+')" class="btn btn-xs btn-success"><i class="fa fa-bolt fa-fw"></a>';
}

function editDevice(data, type, full){
    return '<a type="button" onClick="editDv('+full.id+')" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>'
}

function activateAccount(data, type, full){
    if(full.status === 'ACTIVE')
        return '<a type="button" onclick="warning('+full.id+')" class="btn  btn-xs btn-danger">Warning</a>';
    else if(full.status === 'WARNING')
        return '<a type="button" onclick="deactivate('+full.id+')" class="btn btn-xs btn-danger">Deactivate</a>';
    else
        return '<a type="button" onclick="activate('+full.id+')" class="btn  btn-xs btn-success">Activate</a>' ;
}

function updateUser(data, type, full){
    return (full.username === $('#current-user').val()) ? '' : '<a type="button" onclick="updateUser('+ full.id +')" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>';
}

function monthAndYear(data, type, full){
    return monthNames[full.schedule.month -1]+ ' '+ full.schedule.year;
}
function monthAndYearPayment(data, type, full){
    return monthNames[full.invoice.schedule.month -1]+ ' '+ full.invoice.schedule.year;
}
function createPaymentAction(data, type, full){
    if (full.payment == null)
        return '---';
    else return '<a type="button" onclick="checkCanEdit('+ full.payment.id +')" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>';
}
function expenseType(data, type, full){
    var type = ["", "Wage(1-15)", "Wage(16-30)", "Power Usage"];
    return type[data];
}