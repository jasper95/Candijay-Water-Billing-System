package com.service;

import com.domain.Account;
import com.domain.Message;

import java.util.List;

/**
 * Created by Bert on 11/22/2016.
 */
public interface MessagingService {
    List<Account> sendMessages(List<Account> accounts, Message template) throws Exception;
    void saveTemplate(Message message);
    boolean isReadyToSendMessages();
}