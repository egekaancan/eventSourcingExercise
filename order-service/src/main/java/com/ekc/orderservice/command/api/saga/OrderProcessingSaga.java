package com.ekc.orderservice.command.api.saga;

import com.ekc.commonservice.commands.*;
import com.ekc.commonservice.events.*;
import com.ekc.commonservice.model.User;
import com.ekc.commonservice.queries.GetUserPaymentDetailsQuery;
import com.ekc.orderservice.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for OrderId: {}", event.getOrderId());

        GetUserPaymentDetailsQuery query = new GetUserPaymentDetailsQuery(event.getUserId());

        User user = null;

        try{
            user = queryGateway.query(query, ResponseTypes.instanceOf(User.class))
                    .join();
        }catch (Exception e){
            log.error(e.getMessage());
            cancelOrderCommand(event.getOrderId());
        }

        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand
                .builder()
                .cardDetails(user.getCardDetails())
                .orderId(event.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in Saga for OrderId: {} ", event.getOrderId());
        try{
            ShipOrderCommand command = ShipOrderCommand
                    .builder()
                    .orderId(event.getOrderId())
                    .shipmentId(UUID.randomUUID().toString())
                    .build();

            commandGateway.send(command);
        }catch(Exception e){
            log.error(e.getMessage());
            cancelPaymentCommand(event);
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderShippedEvent event) {
        log.info("OrderShippedEvent in Saga for OrderId: {}", event.getOrderId());
        CompleteOrderCommand command = CompleteOrderCommand
                .builder()
                .orderId(event.getOrderId())
                .orderStatus("APPROVED")
                .build();

        commandGateway.sendAndWait(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in Saga for OrderId: {}", event.getOrderId());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCancelledEvent event) {
        log.info("OrderCancelledEvent in Saga for OrderId: {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent event) {
        log.info("PaymentCancelledEvent in Saga for OrderId: {}", event.getOrderId());
        cancelOrderCommand(event.getOrderId());
    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand command = new CancelOrderCommand(orderId);
        commandGateway.send(command);
    }

    private void cancelPaymentCommand(PaymentProcessedEvent event){
        CancelPaymentCommand command = new CancelPaymentCommand(event.getPaymentId(), event.getOrderId());
        commandGateway.send(command);
    }
}
