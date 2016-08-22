package com.service.impl;

import com.dao.springdatajpa.ExpenseRepository;
import com.dao.springdatajpa.ModifiedExpenseRepository;
import com.dao.springdatajpa.ScheduleRepository;
import com.domain.Expense;
import com.domain.ModifiedExpense;
import com.domain.Schedule;
import com.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Service
public class ExpenseServiceImpl implements ExpenseService{

    private ScheduleRepository schedRepo;
    private ExpenseRepository expenseRepo;
    private ModifiedExpenseRepository modifiedExpenseRepo;

    @Autowired
    public ExpenseServiceImpl(ScheduleRepository schedRepo, ExpenseRepository expenseRepo, ModifiedExpenseRepository modifiedExpenseRepo){
        this.schedRepo = schedRepo;
        this.expenseRepo = expenseRepo;
        this.modifiedExpenseRepo = modifiedExpenseRepo;
    }

    @Transactional(readOnly=true)
    @Override
    public Errors validateExpenseForm(Expense form, Errors result) {
        Schedule sched = schedRepo.findByMonthAndYear(form.getSchedule().getMonth(), form.getSchedule().getYear());
        if(sched != null){
            Expense uniqueExpense = expenseRepo.findByScheduleAndType(sched, form.getType());
            if(uniqueExpense != null && (form.getId() == null || (form.getId() != null && !form.getId().equals(uniqueExpense.getId())))){
                result.rejectValue("schedule.month","","");
                result.rejectValue("schedule.year","","");
                result.rejectValue("type","","");
                result.reject("global", "Duplicate type of expense for this schedule");
            }else {
                form.setSchedule(sched);
            }
        } else form.setSchedule(new Schedule(form.getSchedule().getMonth(), form.getSchedule().getYear()));
        return result;
    }

    @Override
    @Transactional
    public Expense saveExpense(Expense form) {
        if(form.getId() != null){
            Expense expense = expenseRepo.findOne(form.getId());
            ModifiedExpense oldExpense = new ModifiedExpense(expense);
            Long oldVersion = expense.getVersion();
            Schedule sched = schedRepo.save(form.getSchedule());
            expense.setSchedule(sched);
            expense.setAmount(form.getAmount());
            expense.setType(form.getType());
            expense.setVersion(form.getVersion());
            expense = expenseRepo.saveAndFlush(expense);
            if(expense.getVersion().compareTo(oldVersion) > 0){
                oldExpense.setExpense(expense);
                modifiedExpenseRepo.save(oldExpense);
            }
            return expense;

        }else{
            Schedule sched = schedRepo.save(form.getSchedule());
            form.setSchedule(sched);
            return expenseRepo.save(form);
        }
    }
}
