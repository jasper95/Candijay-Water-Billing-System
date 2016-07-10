function checkCanEdit(id) {
    $.getJSON("payments/" + id + "/check-can-edit", function(data) {
        if(data.status === "SUCCESS"){ 
            window.location.href = $('#request-uri').val()+"/"+id+"/edit";    
        }
        else alert("You can only edit the payment for latest invoice");
    });
}
