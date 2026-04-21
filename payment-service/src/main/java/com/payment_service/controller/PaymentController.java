package com.payment_service.controller;

import com.payment_service.client.OrderClient;
import com.payment_service.service.PaymentService;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout/{orderId}")
    public ResponseEntity<?> createPaymentSession(@PathVariable Long orderId){

        Session paymentUrl = paymentService.createCheckoutSession(orderId,3000L);
        return ResponseEntity.ok(Map.of(
                "message", "Stripe session Created",
                "paymenturl", paymentUrl.getUrl()
        ));
    }



    @GetMapping("/success")
    public  String paymentSuccess(@RequestParam Long orderId){

        //Update order Status

        boolean status = paymentService.markOrderAsPaid(orderId);

            return  "Payment Successful for OrderId: " + orderId;


    }
}
