package com.controller.rest;

import com.domain.*;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.service.DataTableService;
import com.service.MeterReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/admin/api/datatables", name="datatables-api", headers = "x-requested-with=XMLHttpRequest")
public class DatatablesApiController {

    private DataTableService dataTableService;
    private MeterReadingService mrService;

    @Autowired
    public DatatablesApiController(DataTableService dataTableService, MeterReadingService mrService){
        this.dataTableService = dataTableService;
        this.mrService = mrService;
    }
    /**
     *  account datatable web-service handler.
     */
    @RequestMapping(value = "/accounts", name="accounts")
    public DatatablesResponse<Account> getAccountDataTable(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<Account> dataSet = dataTableService.findWithDataTableCriterias(criterias, Account.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/customers", name="customers")
    public DatatablesResponse<Customer> getCustomerDataTable(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<Customer> dataSet = dataTableService.findWithDataTableCriterias(criterias, Customer.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/readings", name="readings")
    public DatatablesResponse<MeterReading> getReadingDataTable(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<MeterReading> dataSet = dataTableService.findWithDataTableCriterias(criterias, MeterReading.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/readings/modified", name="modified-readings")
    public DatatablesResponse<ModifiedReading> getReadingsModifiedDataTable(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<ModifiedReading> dataSet = dataTableService.findWithDataTableCriterias(criterias, ModifiedReading.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/readings/accounts-no-reading", name="accounts-tbr")
    public DatatablesResponse<Account> getAccountsNoReadingDataTable(@DatatablesParams DatatablesCriterias criterias){
        DataSet<Account> dataSet = mrService.findAccountsWithCustomParams(criterias);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/bills", name="bills")
    public DatatablesResponse<Invoice> getInvoiceDataTable(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<Invoice> dataSet = dataTableService.findWithDataTableCriterias(criterias, Invoice.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/expenses", name="expenses")
    public DatatablesResponse<Expense> getExpenseDataTable(@DatatablesParams DatatablesCriterias criterias){
        DataSet<Expense> dataSet = dataTableService.findWithDataTableCriterias(criterias, Expense.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/expenses/modified", name="modified-expenses")
    public DatatablesResponse<ModifiedExpense> getModifiedExpenseDataTable(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<ModifiedExpense> dataSet = dataTableService.findWithDataTableCriterias(criterias, ModifiedExpense.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/payments", name="payments")
    public DatatablesResponse<Payment> getPaymentsDataTable(@DatatablesParams DatatablesCriterias criterias) {
        DataSet<Payment> dataSet = dataTableService.findWithDataTableCriterias(criterias, Payment.class);
        return DatatablesResponse.build(dataSet, criterias);
    }

    @RequestMapping(value = "/devices", name="devices")
    public DatatablesResponse<Device> getDeviceDataTable(@DatatablesParams DatatablesCriterias criterias) {
        System.out.println("wews");
        DataSet<Device> dataSet = dataTableService.findWithDataTableCriterias(criterias, Device.class);
        return DatatablesResponse.build(dataSet, criterias);
    }
}
