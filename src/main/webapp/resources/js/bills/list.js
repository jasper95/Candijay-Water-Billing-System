$(document).ready(function(){
    $('#master-checkbox').click(function(e) {
        var chk = $(this).prop('checked');
        $('input', oTable_invoice.$('tr', {"filter": "applied"} )).prop('checked',chk);
    });
    $('#form').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        $.post(form.attr('action')+'/print-check', form.serialize(), function(response){
            if(response.status === "SUCCESS")
                openReport('POST', form.attr('action')+'/print', response.result, '_blank');
        })
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
});