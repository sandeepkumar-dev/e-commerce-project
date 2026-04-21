package com.payment_service.service;

import com.payment_service.client.OrderClient;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private OrderClient  orderClient;

    public PaymentService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    public Session createCheckoutSession(Long orderId, long amount) {

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8084/api/v1/payment/success?orderId=" + orderId)
                        .setCancelUrl("http://localhost:8084/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount(amount)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Order Payment")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        try {
            return Session.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Stripe Session", e);
        }
    }

    public boolean markOrderAsPaid(long orderId) {

        //Write the code to update the status

        boolean status = orderClient.updateOrderStatus(orderId);
        return status;
    }
}