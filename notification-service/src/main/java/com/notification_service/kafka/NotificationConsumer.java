package com.notification_service.kafka;

import com.notification_service.dto.OrderEvent;
import com.notification_service.service.EmailService;
import com.notification_service.service.SmsService;
import com.notification_service.service.WhatsAppService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final EmailService emailService;
    private final SmsService smsService ;
    private  final WhatsAppService whatsAppService;

    public NotificationConsumer(EmailService emailService, SmsService smsService, WhatsAppService whatsAppService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.whatsAppService = whatsAppService;
    }

    @KafkaListener(
            topics = "order-events",
            groupId = "notification-group"
    )
    public void consume(OrderEvent event) {

        System.out.println("Event Received : " + event.getEmail());

        if(event.getStatus().equals("Success")){
            emailService.sendEmail(event.getEmail(), "Transaction Completed", "Your order is placed successfully: "+ event.getOrderId());
            smsService.sendSms(event.getMobile(),"Transaction Completed");
            whatsAppService.sendWhatsAppMessage(event.getMobile(), "Transaction Completed");
        }else {
            emailService.sendEmail(event.getEmail(), "Transaction Incompleted", "Your order is not placed successfully: "+ event.getOrderId());
            smsService.sendSms(event.getMobile(),"Transaction InCompleted");
            whatsAppService.sendWhatsAppMessage(event.getMobile(), "Transaction InCompleted");
        }



        String subject = "Order Status Update";

        String body =
                "Order ID : " + event.getOrderId() +
                        "\\nStatus : " + event.getStatus();

        System.out.println(subject);
        System.out.println(body);
    }
}