package com.service;

import com.domain.Expense;
import com.domain.ModifiedExpense;
import org.springframework.validation.Errors;

/**
 * Created by jasper on 7/28/16.
 */
public interface ExpenseService {
    Errors validateExpenseForm(Expense form, Errors result);
    Expense saveExpense(Expense form);
}
