package com.ekc.shipmentservice.command.api.aggregate;

import com.ekc.commonservice.commands.ShipOrderCommand;
import com.ekc.commonservice.events.OrderShippedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class ShipmentAggregate {

    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;

    public ShipmentAggregate() {
    }

    @CommandHandler
    public ShipmentAggregate(ShipOrderCommand command) {
        log.info("Executing ShipOrderCommand for OrderId: {} and ShipmentId: {}",
                command.getOrderId(), command.getShipmentId());

        OrderShippedEvent event =
                new OrderShippedEvent(command.getShipmentId(), command.getOrderId(), "COMPLETED");

        AggregateLifecycle.apply(event);

        log.info("OrderShipmentEvent applied");
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent event) {
        this.shipmentId = event.getShipmentId();
        this.orderId = event.getOrderId();
        this.shipmentStatus = event.getShipmentStatus();
    }
}
