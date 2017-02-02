package com.controller;


import com.dao.springdatajpa.ExpenseRepository;
import com.domain.Expense;
import com.service.DataTableService;
import com.service.ExpenseService;
import com.service.FormOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jasper on 7/26/16.
 */
@RequestMapping("/admin/expenses")
@Controller
public class ExpenseController {

    private FormOptionsService formOptionsService;
    private ExpenseService expenseService;
    private ExpenseRepository expenseRepo;

    @Autowired
    public ExpenseController(FormOptionsService formOptionsService,
                             ExpenseService expenseService, ExpenseRepository expenseRepo){
        this.formOptionsService = formOptionsService;
        this.expenseService = expenseService;
        this.expenseRepo = expenseRepo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(ModelMap model){
        HashMap options = formOptionsService.getExpenseFormOptions();
        model.put("expenseForm", new Expense());
        model.put("yearOptions", options.get("year"));
        model.put("monthOptions", options.get("month"));
        model.put("typeOptions", options.get("typeExpense"));
        return "expenses/expenseList";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody HashMap saveExpense(@ModelAttribute("expenseForm") @Valid Expense expenseForm, BindingResult result,
                                             @RequestParam Map<String, String> params){
        HashMap response = new HashMap();
        if(!result.hasErrors()){
            if(params.get("update").trim().length() > 0)
                expenseForm.setId(Long.valueOf(params.get("update")));
            expenseService.validateExpenseForm(expenseForm, result);
        }
        if(!result.hasErrors()){
            try{
                Expense expense = expenseService.saveExpense(expenseForm);
                response.put("expense",expense);
            }catch(JpaOptimisticLockingFailureException e){
                result.reject("global", "This record was modified by another user. Try refreshing the page.");
            }catch(Exception e){
                result.reject("global", "An unexpected error occurred while saving the data. Please report it to the developer.");
            }
        }
        if(result.hasErrors()){
            response.put("status", "FAILURE");
            response.put("result", result.getAllErrors());
            return response;
        }
        response.put("status", "SUCCESS");
        return response;
    }

    @RequestMapping(value="/find", method=RequestMethod.POST)
    public @ResponseBody HashMap findExpense(@RequestParam Map<String, String> params){
        HashMap response = new HashMap();
        Expense expense = null;
        try{
            Long id = Long.valueOf(params.get("id"));
            if(id != null)
                expense = expenseRepo.findOne(id);
            if(expense != null){
                response.put("status", "SUCCESS");
                response.put("expense", expense);
            }
        }catch(Exception e){
            response.put("status", "FAILURE");
            return response;
        }
        return response;
    }
}
