/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.domain.*;
import com.forms.PaymentForm;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import net.sf.jasperreports.engine.JRDataSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * interface created for services used in payment module.
 * @author jasper
 */
public interface PaymentService {
    JRDataSource paymentHistoryDataSource(List<Long> paymentIds);
    Payment save(PaymentForm form);
    BindingResult validate(PaymentForm form, BindingResult errors);
    boolean isAllowedToSetWarningToAccount(Account account, Integer debtsAllowed);
    List<Account> updateAccountsWithNoPayments(Address address);
    JRDataSource getPreviousPaymentsDataSource(Account account);
}