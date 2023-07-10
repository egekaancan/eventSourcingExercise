package com.ekc.paymentservice.command.api.events;

import com.ekc.commonservice.events.PaymentCancelledEvent;
import com.ekc.commonservice.events.PaymentProcessedEvent;
import com.ekc.paymentservice.command.api.entity.Payment;
import com.ekc.paymentservice.command.api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class PaymentEventsHandler {

    private final PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment = Payment.builder()
                .paymentId(event.getPaymentId())
                .paymentStatus("COMPLETED")
                .orderId(event.getOrderId())
                .timeStamp(new Date())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId()).get();
        payment.setPaymentStatus(event.getPaymentStatus());
        paymentRepository.save(payment);
    }
}
