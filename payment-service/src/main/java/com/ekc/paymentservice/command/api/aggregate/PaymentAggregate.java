package com.ekc.paymentservice.command.api.aggregate;

import com.ekc.commonservice.commands.CancelPaymentCommand;
import com.ekc.commonservice.commands.ValidatePaymentCommand;
import com.ekc.commonservice.events.PaymentCancelledEvent;
import com.ekc.commonservice.events.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;

    public PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand command) {
        log.info("Executing ValidatePaymentCommand for OrderId: {} and PaymentId: {}",
                command.getOrderId(), command.getPaymentId());

        PaymentProcessedEvent event
                = new PaymentProcessedEvent(command.getPaymentId(), command.getOrderId());

        AggregateLifecycle.apply(event);

        log.info("PaymentProcessedEvent applied");
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }

    @CommandHandler
    public void handle(CancelPaymentCommand command) {
        log.info("Executing CancelPaymentCommand for OrderId: {} and PaymentId: {}",
                command.getOrderId(), command.getPaymentId());
        PaymentCancelledEvent event = new PaymentCancelledEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
        log.info("PaymentCancelledEvent applied");
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }
}
