package com.service.impl;

import com.domain.Account;
import com.domain.Message;
import com.service.MessagingService;
import org.smslib.OutboundMessage;

import org.smslib.helper.CommPortIdentifier;
import org.smslib.modem.SerialModemGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bert on 11/22/2016.
 */
@Service("localMessaging")
public class MessagingServiceImpl implements MessagingService {

    private boolean testPort(String port){
        SerialModemGateway gateway = new SerialModemGateway("m", port, 115200, "", "");
        OutboundMessage test = new OutboundMessage("+639995867138", "This message tests connection between the modem and the server. Please ignore this message.");
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSmscNumber("+639180000101");
        try{
            org.smslib.Service.getInstance().addGateway(gateway);
            org.smslib.Service.getInstance().startService();
            boolean testStatus = org.smslib.Service.getInstance().sendMessage(test);
            org.smslib.Service.getInstance().stopService();
            org.smslib.Service.getInstance().removeGateway(gateway);
            return testStatus;
        }catch (Exception e){
            try{
                org.smslib.Service.getInstance().removeGateway(gateway);
            }catch(Exception e1){

            }
        }
        return false;
    }

    private String findAndSetCurrentPort() throws Exception {
        CommPortIdentifier portId;
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                boolean comPortSuccess = testPort(portId.getName());
                if(comPortSuccess)
                    return portId.getName();
            }
        }
        throw new Exception("Modem not connected. Please ensure the modem is plugged in and try again.");
    }

    @Override
    public List<Account> sendMessages(List<Account> accounts, Message template) throws Exception {
        String port = "COM15";
        List<OutboundMessage> messages = new ArrayList();
        String messageTemplate = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore "
                + "et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
                + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
                + "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
        /*for(Account account : accounts)
            messages.add(new OutboundMessage(account.getCustomer().getContactNumber(), template.getBody()));*/
        HashMap<String, Account> accountContactMap = new HashMap();
        for(Account account : accounts) {
            messages.add(new OutboundMessage(account.getCustomer().getContactNumber(), messageTemplate));
            accountContactMap.put(account.getCustomer().getContactNumber(), account);
        }
        SerialModemGateway gateway = new SerialModemGateway("m", port, 115200, "", "");
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSmscNumber("+639180000101");
        org.smslib.Service.getInstance().addGateway(gateway);
        org.smslib.Service.getInstance().startService();
        org.smslib.Service.getInstance().sendMessages(messages);
        org.smslib.Service.getInstance().stopService();
        org.smslib.Service.getInstance().removeGateway(gateway);
        List<Account> failedMessagesSentToAccounts = new ArrayList();
        for(OutboundMessage message : messages){
            failedMessagesSentToAccounts.add(accountContactMap.get(message.getRecipient()));
        }
        return failedMessagesSentToAccounts;

    }

    @Transactional
    @Override
    public void saveTemplate(Message message) {

    }

    @Override
    public boolean isReadyToSendMessages() {
        return false;
    }
}
