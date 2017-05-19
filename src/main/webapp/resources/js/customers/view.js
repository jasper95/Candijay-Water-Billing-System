$(document).ready(function(){
    window.activate = function (id){
        ajax($('#context-path').val()+'/admin/accounts/activate', { "accountId" : id});
    };
    window.deactivate = function (id){
        ajax($('#context-path').val()+'/admin/accounts/deactivate', { "accountId" : id});
    };
    window.warning = function (id){
        ajax($('#context-path').val()+'/admin/accounts/warning', { "accountId" : id});
    };
    window.ajax = function (url, data){
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
    };
    $('#acct-form').on('submit', function(e){
        e.preventDefault();
        cleanUpFormMsgs('#acct-form');
        $.post($('#context-path').val()+'/admin/accounts/new', $(this).serialize(), function(response){
            if (validateForm('#acct-form', response)) {
                showSuccess('#acct-form', "Account successfully created");
                window.location = $('#context-path').val()+'/admin/accounts/'+response.result.number
            }
        });
    });
    $('#allow-dup-mc-toggle').on('change', function(){
        var mcToggle = $('#allow-dup-mc');
        if(mcToggle.val() === '0')
            mcToggle.val('1');
        else
            mcToggle.val('0');
    });
});