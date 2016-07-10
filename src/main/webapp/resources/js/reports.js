$(document).ready(function(){
    $("#summary-toggle").click(function(){
        var summary = $('#summary').val();
        if(summary == 0){
            $('#barangay-fieldh').hide();
            $(this).removeClass().addClass("animate2s toggle-on");
            $('#summary').val('1');
            $('#barangay').val('');
        } else {
            $('#barangay-fieldh').show();
            $(this).removeClass().addClass('animate2s toggle-off ');
            $('#summary').val('0');
            //$('#barangay').val('');
        }        
    });
    $("#chart-toggle").click(function(){
        var chart = $('#chart').val();
        if(chart == 0){
            $('#not-for-chart').hide();
            $(this).removeClass().addClass("animate2s toggle-on");
            $('#chart').val('1');
            $('#for-chart').show();
        } else {
            $('#not-for-chart').show();
            $('#for-chart').hide();
            $(this).removeClass().addClass('animate2s toggle-off ');
            $('#chart').val('0');
        }        
    });
    $('#reportForm').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        $.post($(this).attr('action')+'/validate-report-form', $(this).serialize(), function(response){
            if(response.status === "SUCCESS")
                openReport('POST', form.attr('action')+'/print-report', response.result, '_blank');
            else displayErrors(response.result);
        });
    });
    $('#chartForm').on('submit', function(e){
        e.preventDefault();
        var form = $(this);
        $.post($(this).attr('action')+'/validate-chart-form', $(this).serialize(), function(response){
            if(response.status === "SUCCESS")
                openReport('POST', form.attr('action')+'/print-chart', response.result, '_blank');
            else displayErrors(response.result);
        });
    });
    $('#submit').on('click', function(e){
        e.preventDefault();
        if($('#chart').val() === '1'){
            $('#chartForm').submit();
        } else {
            $('#reportForm').submit();
        }
    });
    function openReport(verb, url, data, target){
        var form = document.createElement("form");
        form.action = url;
        form.method = verb;
        form.target = target || "_self";
        if (data) {
            for (var key in data) {
              var input = document.createElement("textarea");
              input.name = key;
              input.value = typeof data[key] === "object" ? JSON.stringify(data[key]) : data[key];
              form.appendChild(input);
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
    
    function displayErrors(errors){
        
    }
    
});