$(document).ready(function(){
    window.openReport = function (verb, url, data, target){
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
                    console.log(input.value)
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