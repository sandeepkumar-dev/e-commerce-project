package com.notification_service.service;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    @Value("${twilio.whatsapp.number}")
    private String fromNumber;

    public String sendWhatsAppMessage(String to, String messageBody) {

        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();

        return "WhatsApp Message Sent Successfully. SID: "
                + message.getSid();
    }
}
