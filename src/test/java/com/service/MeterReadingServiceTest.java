package com.service;

import com.annotations.ServiceTest;
import com.dao.springdatajpa.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ServiceTest
public class MeterReadingServiceTest {

    @Autowired
    private MeterReadingService mrService;

    @Autowired
    private AccountRepository accountRepo;


    @Test
    public void saveReadingTests(){

    }

    @Test
    public void updateReading(){

    }

    @Test
    public void deleteReading(){

    }

}
