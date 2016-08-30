$.fn.dataTable.ext.errMode = 'none';
$(document.body).on('xhr.dt', function (e, settings, json, xhr){
    // If there is an Ajax error and status code is 405
    if(json === null && xhr.status === 405){
        BootstrapDialog.alert({
            title: 'Session Expired',
            message: 'You were logged out somewhere or you were logged out due to inactivity. Please login again.',
            type: BootstrapDialog.TYPE_DANGER,
            callback : function(){
                window.location.reload();
            }
        });
    }
});