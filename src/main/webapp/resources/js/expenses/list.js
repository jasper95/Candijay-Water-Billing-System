$(document).ready(function(){
    window.viewChanges = function(id){
        $('#auditExpense-id').find('input:first').val(id);
        $('#filterButtonAuditTable').trigger('click');
        $('#expense-info-modal').modal('show');
    };
    $('#create-expense').click(function(){
        $('#expense-action').text('Create');
        $('#expense-form-modal').modal('show');
        cleanUpFormMsgs('#expense-form');
        cleanUpFormFields('#expense-form');
        $('p.pm-audit').hide();
        $('#exp-version').val('');
        $('#expId').val('')
    });
    $('#expense-form').on('submit', function (e) {
        e.preventDefault();
        var form = $(this);
        cleanUpFormMsgs('#expense-form');
        $.post(form.attr('action'), form.serialize(), function(response){
            if(validateForm('#expense-form', response)){
                var action = ($('expense-action').text() === 'Create') ? 'added' : 'updated';
                showSuccess('#expense-form', "Expense successfully "+action);
                if(action === 'updated'){
                    var expense = response.expense;
                    var typeArray = ["", "Wage(1-15)", "Wage(16-20)", "Power Usage"]
                    var row = $('#expense tbody tr:nth-child('+$('#row-num').val()+')');
                    row.find('.month').text(months[expense.schedule.month])
                    row.find('.year').text(expense.schedule.year)
                    row.find('.type').text(typeArray[expense.type])
                    row.find('.amount').html('&#8369 '+expense.amount);
                    $('#exp-version').val(expense.version);
                }
            }
        });
    });
    $('#expense').on('click', 'tr', function(){
        $('#row-num').val($(this).index()+1)
    });
    window.checkCanEdit = function(id){
        var uri = $('#expense-uri').val()+"find";
        var data = {"id": id};
        var header = $("meta[name='_csrf_header']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");
        $.ajax({
            data : data,
            url : uri,
            type : "POST",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success : function(response){
                if(response.status === "SUCCESS"){
                    cleanUpFormMsgs('#expense-form');
                    cleanUpFormFields('#expense-form');
                    var expense = response.expense;
                    $('#exp-month').val(expense.schedule.month);
                    $('#exp-year').val(expense.schedule.year);
                    $('#exp-type').val(expense.type);
                    $('#exp-amount').val(expense.amount);
                    $('#exp-version').val(expense.version);
                    $('#expId').val(expense.id);
                    $('#cr-time-audit-pm').text(expense.creationTime);
                    $('#up-time-audit-pm').text(expense.modificationTime);
                    $('#cr-user-audit-pm').text(expense.createdByUser);
                    $('#up-user-audit-pm').text(expense.modifiedByUser);
                    $('p.pm-audit').show();
                    $('#expense-action').text('Update');
                    $('#expense-form-modal').modal('show');
                } else alert("This expense might have been deleted by other user. Please refresh the page.")
            }
        });
    };
});