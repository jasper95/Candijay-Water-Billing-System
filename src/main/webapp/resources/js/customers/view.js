function activate(id){
    ajax($('#context-path').val()+'/admin/accounts/activate', { "accountId" : id});
}
function deactivate(id){
    ajax($('#context-path').val()+'/admin/accounts/deactivate', { "accountId" : id});
}
function warning(id){
    ajax($('#context-path').val()+'/admin/accounts/warning', { "accountId" : id});
}
function ajax(url, data){
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url : url,
        data : data,
        type : "POST",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(response){
            if(response.status === "SUCCESS")
                $('#reload').trigger('click');
        }
    });
}

