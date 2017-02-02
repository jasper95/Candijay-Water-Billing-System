var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
];
function customerUrl(data, type, full){
    return '<a href="/admin/customers/'+full.id+'/"> '+data+'</a>';
};
function customerUrlReadingList(data, type, full){
    return '<a href="/admin/customers/'+full.account.customer.id+'/"> '+data+'</a>';
};
function customerListActions(data, type, full){
    return '<a type="button" href="/admin/customers/'+full.id+'/update/" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>';
};
function accountUrl(data, type, full){
    return '<a href="/admin/accounts/'+data+'/"> '+data+'</a>';
};
function checkboxAccount(data, type, full){
    return full.statusUpdated ? '<input type="checkbox" name="checkboxValues" value="'+full.id+'" />' : '';
};
function checkboxPayment(data, type, full){
    return full.amountPaid > 0 ? '<input type="checkbox" name="checkboxValues" value="'+full.id+'" />' : '';
};
function toPeso(data, type, full){
    if (data == null) return '---';
    else return "&#8369; "+data;
};
function month(data, type, full){
    return monthNames[data-1];
};
function checkbox(data, type, full){
    return '<input type="checkbox" name="checkboxValues" value="'+full.id+'" />';
};
function paymentListActions(data, type, full){
    return '<a type="button" onclick="checkCanEdit('+ full.id +')" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>';
};
function userListUsername(data, type, full){
    return (data === $('#current-user').val()) ? data+'(You)' : data;
};
function trueToYes(data, type, full){
    return (data) ? 'Yes' : 'No';
};
function activateDevice(data, type, full){
    return (full.active) ? '': '<a type="button" onclick="activate('+full.id+')" class="btn btn-xs btn-success"><i class="fa fa-bolt fa-fw"></i></a>';
};
function editDevice(data, type, full){
    return '<a type="button" onClick="editDv('+full.id+')" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>';
};
function activateAccount(data, type, full){
    if(full.status === 'ACTIVE') return '<a type="button" onclick="warning('+full.id+')" class="btn  btn-xs btn-danger">Warning</a>';
    else if(full.status === 'WARNING') return '<a type="button" onclick="deactivate('+full.id+')" class="btn btn-xs btn-danger">Deactivate</a>';
    else return '<a type="button" onclick="activate('+full.id+')" class="btn  btn-xs btn-success">Activate</a>' ;
};
function updateUser(data, type, full){
    return (full.username === $('#current-user').val()) ? '' : '<a type="button" onclick="updateUser('+ full.id +')" class="btn btn-xs btn-primary"><i class="fa fa-edit fa-fw"></i></a>';
};
function expenseType(data, type, full){
    var type = ["", "Wage(1-15)", "Wage(16-30)", "Power Usage"];
    return type[data];
};
function billsListActions(data, type, full){
    if(full.status === "UNPAID") return '<a type="button" onClick="editDisc('+full.id+')" class="btn btn-xs btn-primary"><i class="fa fa-tag fa-fw"></i></a>';
    else return '';
};
function accountRecentPayments(data, type, full){
    return '<a type="button" onClick="viewRecentPayments('+full.id+')" class="btn btn-xs btn-primary"><i class="fa fa-money fa-fw"></i></a>';
};
function recentReadingsActions(data, type, full){
    var audit = '', del = '', edit = '', discount = '';
    var info = '<a title="View Bill Details" type="button" onclick="viewBillDetails('+ full.invoice.id +')" class="bill-details-btn btn btn-xs btn-primary table-action-btn"><i class="fa fa-question fa-fw"></i></a>';
    if (full.version !== 0) audit = '<a title="Input History" type="button" onclick="viewChanges('+ full.id +')" class="btn btn-xs btn-primary table-action-btn"><i class="fa fa-history fa-fw"></i></a>';
    if (full.invoice.status === "UNPAID") del = '<a title="Delete Reading" type="button" onClick="deleteItem('+full.id+')" class="btn btn-xs btn-danger table-action-btn"><i class="fa fa-remove fa-fw"></i></a>';
    if(full.invoice.status !== "FULLYPAID"){
        edit = '<a title="Edit Reading" type="button" onclick="checkCanEdit('+ full.id +')" class="btn btn-xs btn-primary table-action-btn"><i class="fa fa-edit fa-fw"></i></a>';
        discount = '<a title="Edit Discount" type="button" onClick="editDisc('+full.invoice.id+')" class="btn btn-xs btn-primary"><i class="fa fa-tag fa-fw"></i></a>';
    }
    return info+edit+discount+audit+del;
};
function scheduleAndStatus(data, type, full){
    var actual_status = full.invoice.status;
    if(actual_status === "PARTIALLYPAID" || actual_status === "FULLYPAID") actual_status = "PAID";
    var res = monthNames[full.schedule.month -1]+ ' '+ full.schedule.year+'('+actual_status+')';
    return '<a href="" onClick="billPrintablePreview('+full.invoice.id+'); return false"> '+ res+'</a>';
};
function monthAndYear(data, type, full){
    var actual_status = full.status;
    if(actual_status === "PARTIALLYPAID" || actual_status === "FULLYPAID") actual_status = "PAID";
    var res = monthNames[full.schedule.month -1]+ ' '+ full.schedule.year+'('+actual_status+')';
    return '<a href="" onClick="billPrintablePreview('+full.id+'); return false"> '+res+'</a>';
};
function recentBillsActions(data,type, full){
    var discount = '', info = '<a title="View Bill Details" type="button" onclick="viewBillDetails('+ full.id +')" class="bill-details-btn btn btn-xs btn-primary table-action-btn"><i class="fa fa-question fa-fw"></i></a>';
    if(full.status !== "FULLYPAID") discount = '<a title="Edit Discount" type="button" onClick="editDisc('+full.id+')" class="btn btn-xs btn-primary"><i class="fa fa-tag fa-fw"></i></a>';
    return info+discount;
};
function chooseAccount(data, type, full){
    var accNum = "\'"+data+"\'";
    return '<a href="" onClick="selectAccount('+accNum+'); return false">'+data+'</a>';
};
function readingListActions(data, type, full){
    var audit = '';
    if (full.version !== 0) audit = '<a title="Input History" type="button" onclick="viewChanges('+ full.id +')" class="btn btn-xs btn-primary table-action-btn"><i class="fa fa-history fa-fw"></i></a>';
    return audit;
};