package com.ekc.shipmentservice.command.api.events;

import com.ekc.commonservice.events.OrderShippedEvent;
import com.ekc.shipmentservice.command.api.entity.Shipment;
import com.ekc.shipmentservice.command.api.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipmentEventsHandler {

    private final ShipmentRepository shipmentRepository;

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(event, shipment);
        shipmentRepository.save(shipment);
    }
}
