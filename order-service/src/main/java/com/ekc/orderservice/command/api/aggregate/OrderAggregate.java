package com.ekc.orderservice.command.api.aggregate;

import com.ekc.commonservice.commands.CancelOrderCommand;
import com.ekc.commonservice.commands.CompleteOrderCommand;
import com.ekc.commonservice.events.OrderCancelledEvent;
import com.ekc.orderservice.command.api.commands.CreateOrderCommand;
import com.ekc.commonservice.events.OrderCompletedEvent;
import com.ekc.orderservice.command.api.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private int quantity;
    private String orderStatus;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderStatus = event.getOrderStatus();
        this.quantity = event.getQuantity();
        this.addressId = event.getAddressId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.orderId = event.getOrderId();
    }

    @CommandHandler
    public OrderAggregate(CompleteOrderCommand command){
        OrderCompletedEvent event = OrderCompletedEvent
                .builder()
                .orderId(command.getOrderId())
                .orderStatus(command.getOrderStatus())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(CancelOrderCommand command) {
        OrderCancelledEvent event = new OrderCancelledEvent();
        BeanUtils.copyProperties(command,event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        this.orderStatus = event.getOrderStatus();
    }
}
