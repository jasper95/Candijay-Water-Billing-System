$(document).ready(function(){
    $('#master-checkbox').click(function(e) {
        var chk = $(this).prop('checked');
        $('input', oTable_account.$('tr', {"filter": "applied"} )).prop('checked',chk);
    });
    $('#apply').on('click', function(){
        var action = $('#action').val(), form = $('#form');
        if(action === '1' || action === '2'){
            $.post(form.attr('action')+'/deactivate-check', form.serialize(), function(response){
                if(response.status === "SUCCESS"){
                    if(action === '1')
                        openReport('POST',form.attr('action')+"/print-notice-of-disconnection",response.result,'_blank');
                    else if( action  === '2')
                        changeStatus(form.serialize(), form.attr('action')+'/deactivate-accounts');
                } else alert("No account is qualified for the action");
            });
        }
        else if(action === "3")
            changeStatus(form.serialize(), form.attr('action')+'/warning-accounts');
        else if(action === "4")
            changeStatus(form.serialize(), form.attr('action')+'/activate-accounts');

    });
    function openReport(verb, url, data, target){
        var form = document.createElement("form");
        form.action = url;
        form.method = verb;
        form.target = target || "_self";
        if (data) {
            for (var key in data) {
                if( Object.prototype.toString.call( data[key] ) === '[object Array]' ){
                    for(var i in data[key]){
                        var input = document.createElement("textarea");
                        input.name = key;
                        input.value = data[key][i];
                        form.appendChild(input);
                    }
                }
                else {
                    var input = document.createElement("textarea");
                    input.name = key;
                    input.value = typeof data[key] === "object" ? JSON.stringify(data[key]) : data[key];
                    form.appendChild(input);
                }
            }
        }
        if(verb === 'POST'){
            var input = document.createElement("textarea");
            input.name = '_csrf';
            input.value = $("meta[name='_csrf']").attr("content");
            form.appendChild(input);
        }
        form.style.display = 'none';
        document.body.appendChild(form);
        form.submit();
    }
    function changeStatus(data, url){
        $.post(url, data, function(response){
            if(response.status === "SUCCESS")
                $('#filterButton').trigger('click');
            else alert("No account is qualified for the action");
        });
    }
    
});