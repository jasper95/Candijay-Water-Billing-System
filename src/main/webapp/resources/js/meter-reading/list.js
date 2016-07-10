function checkCanEdit(id) {
    $.getJSON("reading/" + id + "/check-paid", function(data) {
        if(data.status === "SUCCESS"){ 
            window.location.href = "http://localhost:8084/CWSMaven/admin/reading/"+id+"/edit";    
        }
        else alert("You cannot edit a paid reading");
    });
}